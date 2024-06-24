package com.myf.domain.zouding.service;


import com.myf.hleper.model.dto.BoardDTO;
import com.myf.hleper.model.dto.CellDTO;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @Author: myf
 * @CreateTime: 2023-12-13  20:45
 * @Description: FiveChessDomainService
 */
public interface FiveChessDomainService {

    /**
     * 是要要移动,为true说明是要移开某个位置，为false说明是要存放到某个位置
     *
     * @param boardDTO
     * @return
     */
    Boolean isToMove(BoardDTO boardDTO);

    /**
     * 判断哪些位置可以存放
     *
     * @param boardDTO
     * @return
     */
    CellDTO[][] canPlacePosition(BoardDTO boardDTO);


    /**
     * 判断游戏是否结束
     *
     * @param boardDTO
     * @return
     */
    Boolean gameIsOver(BoardDTO boardDTO);


    /**
     * 移动到新位置的逻辑处理
     *
     * @param boardDTO
     * @return
     */
    Pair<CellDTO[][],Boolean> moveToNewPosition(BoardDTO boardDTO);

}
