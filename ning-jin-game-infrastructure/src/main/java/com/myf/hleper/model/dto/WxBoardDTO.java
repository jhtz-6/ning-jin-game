package com.myf.hleper.model.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-04-13  09:48
 * @Description: TODO
 */
@Data
public class WxBoardDTO {

    private CellDTO[] boards =new CellDTO[4];

    private Integer row;

    private Integer column;

    private CellDTO currentCell;

    private CellDTO beforeCell;

    private String otherUserId;

    private String userId;

    private Integer columnNumber;



    public static BoardDTO wxBoardDTO2boardDTO(WxBoardDTO wxBoardDTO){
        BoardDTO boardDTO =new BoardDTO();
        boardDTO.setColumn(wxBoardDTO.getColumn());
        boardDTO.setBeforeCell(wxBoardDTO.getBeforeCell());
        boardDTO.setColumnNumber(wxBoardDTO.getColumnNumber());
        boardDTO.setRow(wxBoardDTO.getRow());
        boardDTO.setUserId(wxBoardDTO.getUserId());
        boardDTO.setOtherUserId(wxBoardDTO.getOtherUserId());
        boardDTO.setCurrentCell(wxBoardDTO.getCurrentCell());
        CellDTO[][] boards =new CellDTO[wxBoardDTO.getColumnNumber()][wxBoardDTO.getColumnNumber()];
        for(int i = 0;i< wxBoardDTO.getColumnNumber();i++){
            for(int j = 0;j< wxBoardDTO.getColumnNumber();j++){
                CellDTO cellDTO =new CellDTO();
                cellDTO.setClickable(false);
                boards[i][j] = cellDTO;
            }
        }
        CellDTO[] wxBoard = wxBoardDTO.getBoards();
        for(int m = 0;m<wxBoard.length;m++){
            boards[wxBoard[m].getRow()][wxBoard[m].getColumn()] = wxBoard[m];
        }
        boardDTO.setBoards(boards);
        return boardDTO;
    }
}
