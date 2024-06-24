package com.myf.zouding.service.impl;


import com.myf.common.enmus.WebSocketMessageType;
import com.myf.domain.zouding.service.FiveChessDomainService;
import com.myf.hleper.model.dto.BoardDTO;
import com.myf.hleper.model.dto.CellDTO;
import com.myf.hleper.model.res.Result;

import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.model.res.HandleCellClickResult;
import com.myf.zouding.service.ZouDingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: myf
 * @CreateTime: 2023-12-13  20:35
 * @Description: TODO
 */
@Service("zouDingService")
public class FiveChessServiceImpl implements ZouDingService {
    private static final Map<Integer,Integer> boardFourMap=new HashMap<>();
    private static final Map<Integer,Integer> boardFiveMap=new HashMap<>();
    private static final Map<Integer,Integer> boardFixMap=new HashMap<>();
    private static final Map<Integer,Map<Integer,Integer>> BOARD_MAP=new HashMap<>();

    static {
        boardFourMap.put(0,3);
        boardFourMap.put(1,2);
        boardFourMap.put(2,1);
        boardFourMap.put(3,0);

        boardFiveMap.put(0,4);
        boardFiveMap.put(1,3);
        boardFiveMap.put(2,2);
        boardFiveMap.put(3,1);
        boardFiveMap.put(4,0);

        boardFixMap.put(0,5);
        boardFixMap.put(1,4);
        boardFixMap.put(2,3);
        boardFixMap.put(3,2);
        boardFixMap.put(4,1);
        boardFixMap.put(5,0);

        BOARD_MAP.put(4,boardFourMap);
        BOARD_MAP.put(5,boardFiveMap);
        BOARD_MAP.put(6,boardFixMap);

    }
    @Autowired
    FiveChessDomainService fiveChessDomainService;

    @Override
    public Pair<Result<HandleCellClickResult>,Result<HandleCellClickResult>> handleCellClick(BoardDTO boardDTO) {
        HandleCellClickResult handleCellClickResult = new HandleCellClickResult();
        HandleCellClickResult otherHandleCellClickResult = new HandleCellClickResult();
        Integer columnNumber = boardDTO.getColumnNumber();
        Map<Integer,Integer> boardMap = BOARD_MAP.get(columnNumber);
        if (fiveChessDomainService.isToMove(boardDTO)) {
            //需要判断哪些位置可以存放
            handleCellClickResult.setCanPlacePosition(fiveChessDomainService.canPlacePosition(boardDTO));
            CellDTO[][] canPlacePosition = handleCellClickResult.getCanPlacePosition();
            CellDTO[][] otherCanPlacePosition = new CellDTO[columnNumber][columnNumber];
            for (int i = 0; i < columnNumber; i++) {
                for (int j = 0; j < columnNumber; j++) {
                    otherCanPlacePosition[i][j] = new CellDTO();
                    BeanUtils.copyProperties(canPlacePosition[boardMap.get(i)][boardMap.get(j)],otherCanPlacePosition[i][j]);
                    otherCanPlacePosition[i][j].setClickable(false);
                }
            }
            otherHandleCellClickResult.setCanPlacePosition(otherCanPlacePosition);
        } else {
            //需要是否结束
            handleCellClickResult.setCanPlacePosition(fiveChessDomainService.moveToNewPosition(boardDTO).getLeft());
            boardDTO.setBoards(handleCellClickResult.getCanPlacePosition());
            Boolean gameIsOver = fiveChessDomainService.gameIsOver(boardDTO);
            handleCellClickResult.setIsEnd(gameIsOver);
            otherHandleCellClickResult.setIsEnd(gameIsOver);
            if (gameIsOver) {
                handleCellClickResult.setWinner(boardDTO.getBeforeCell().getContent());
                otherHandleCellClickResult.setWinner(boardDTO.getBeforeCell().getContent());
            }
            CellDTO[][] canPlacePosition = handleCellClickResult.getCanPlacePosition();
            CellDTO[][] otherCanPlacePosition = new CellDTO[columnNumber][columnNumber];
            for (int i = 0; i < columnNumber; i++) {
                for (int j = 0; j < columnNumber; j++) {
                    otherCanPlacePosition[i][j] = new CellDTO();
                    BeanUtils.copyProperties(canPlacePosition[boardMap.get(i)][boardMap.get(j)],otherCanPlacePosition[i][j]);
                    if(StringUtils.isNotBlank(otherCanPlacePosition[i][j].getContent()) &&
                            !otherCanPlacePosition[i][j].getContent().equals(boardDTO.getCurrentCell().getContent())){
                        otherCanPlacePosition[i][j].setClickable(true);
                    }else{
                        otherCanPlacePosition[i][j].setClickable(false);
                    }
                }
            }
            otherHandleCellClickResult.setCanPlacePosition(otherCanPlacePosition);
        }
        return Pair.of(WebSocketResult.success(handleCellClickResult, WebSocketMessageType.CELL_CLICK),
                WebSocketResult.success(otherHandleCellClickResult,WebSocketMessageType.CELL_CLICK));
    }

}
