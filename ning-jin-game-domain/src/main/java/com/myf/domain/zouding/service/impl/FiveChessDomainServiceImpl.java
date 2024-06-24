package com.myf.domain.zouding.service.impl;


import com.myf.domain.zouding.service.FiveChessDomainService;
import com.myf.hleper.model.dto.BoardDTO;
import com.myf.hleper.model.dto.CellDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;


/**
 * @Author: myf
 * @CreateTime: 2023-12-13  20:45
 * @Description: TODO
 */
@Service("fiveChessDomainService")
public class FiveChessDomainServiceImpl implements FiveChessDomainService {


    @Override
    public Boolean isToMove(BoardDTO boardDTO) {
        return StringUtils.isNotBlank(boardDTO.getBoards()[boardDTO.getRow()][boardDTO.getColumn()].getContent());
    }

    @Override
    public CellDTO[][] canPlacePosition(BoardDTO boardDTO) {
        //只有当前是要移开的步骤才需要调用此方法
        CellDTO[][] boards = boardDTO.getBoards();
        Integer clickedColumns = boardDTO.getColumn();
        Integer clickedRows = boardDTO.getRow();
        CellDTO currentCell = boardDTO.getCurrentCell();
        Integer columnNumber = boardDTO.getColumnNumber();
        CellDTO[][] canPlacePosition = new CellDTO[columnNumber][columnNumber];
        for (int i = 0; i < columnNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                CellDTO cell = boards[i][j];

                CellDTO cellDTO = new CellDTO();
                boolean emptyContentCanPlace = StringUtils.isEmpty(cell.getContent()) && isAdjacent
                        (clickedRows, clickedColumns, i, j);
                boolean clickable = emptyContentCanPlace || currentCell.getContent().equals(cell.getContent());
                cellDTO.setClickable(clickable);
                cellDTO.setContent(cell.getContent());
                cellDTO.setColor(emptyContentCanPlace ? "yellow" : "#282c34");
                cellDTO.setName(emptyContentCanPlace ? "" : cell.getName());
                canPlacePosition[i][j] = cellDTO;
            }
        }
        return canPlacePosition;
    }

    @Override
    public Boolean gameIsOver(BoardDTO board) {
        int xCellNum = 0;
        int oCellNum = 0;
        Integer columnNumber = board.getColumnNumber();
        for (int i = 0; i < columnNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                xCellNum = xCellNum + ("X".equals(board.getBoards()[i][j].getContent()) ? 1 : 0);
                oCellNum = oCellNum + ("O".equals(board.getBoards()[i][j].getContent()) ? 1 : 0);
            }
        }
        //todo 游戏结束 保存记录到数据库 单机和在线都保存 但是要区分
        // 如果某一方棋子小于两颗则游戏结束
        return xCellNum < 2 || oCellNum < 2;
    }

    @Override
    public Pair<CellDTO[][],Boolean> moveToNewPosition(BoardDTO boardDTO) {
        Boolean anyChessEaten = false;
        CellDTO[][] boards = boardDTO.getBoards();
        String currentSide = boardDTO.getBeforeCell().getContent();
        CellDTO currentCellDTO = boards[boardDTO.getRow()][boardDTO.getColumn()];
        currentCellDTO.setClickable(false);
        currentCellDTO.setContent(currentSide);
        currentCellDTO.setColor(currentCellDTO.getColor());
        CellDTO beforeCell = boardDTO.getBeforeCell();
        boards[beforeCell.getRow()][beforeCell.getColumn()].setContent("");
        boards[beforeCell.getRow()][beforeCell.getColumn()].setClickable(false);
        Integer rows = boardDTO.getRow();
        Integer columns = boardDTO.getColumn();
        CellDTO cell = new CellDTO();
        cell.setContent(currentSide);
        boards[boardDTO.getRow()][boardDTO.getColumn()] = cell;
        Integer columnNumber = boardDTO.getColumnNumber();
        CellDTO currentCell = boardDTO.getCurrentCell();
        Integer splitNumber = columnNumber / 2;
        //先判断是否有棋子被吃掉
        String otherSide = "X".equals(currentSide) ? "O" : "X";
        for (int i = 0; i < columnNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if(StringUtils.isBlank(boards[i][j].getContent())){
                    boards[i][j].setName("");
                }
            }
        }
        //先判断行 至少有三个不为空
        for (int c = 0; c < columnNumber - 2; c++) {
            if (StringUtils.isNotBlank(boards[rows][c].getContent()) &&
                    StringUtils.isNotBlank(boards[rows][c + 1].getContent()) &&
                    StringUtils.isNotBlank(boards[rows][c + 2].getContent())) {
                if (c + 3 < columnNumber && StringUtils.isNotBlank(boards[rows][c + 3].getContent())) {
                    continue;
                }
                if (c > 0 && StringUtils.isNotBlank(boards[rows][c - 1].getContent())) {
                    continue;
                }
                //这三个里面必须要有当前的棋子
                if(currentCell.getColumn() != c && currentCell.getColumn() != c+1 && currentCell.getColumn() != c+2){
                    continue;
                }
                if (otherSide.equals(boards[rows][c].getContent()) &&
                        currentSide.equals(boards[rows][c + 1].getContent()) &&
                        currentSide.equals(boards[rows][c + 2].getContent())) {
                    anyChessEaten = true;
                    boards[rows][c].setClickable(false);
                    boards[rows][c].setContent("");
                } else if (otherSide.equals(boards[rows][c + 2].getContent()) &&
                        currentSide.equals(boards[rows][c + 1].getContent()) &&
                        currentSide.equals(boards[rows][c].getContent())) {
                    anyChessEaten = true;
                    boards[rows][c + 2].setClickable(false);
                    boards[rows][c + 2].setContent("");
                }
            }
        }

        //判断列 至少有三个不为空
        for (int r = 0; r < columnNumber - 2; r++) {
            if (StringUtils.isNotBlank(boards[r][columns].getContent()) &&
                    StringUtils.isNotBlank(boards[r + 1][columns].getContent()) &&
                    StringUtils.isNotBlank(boards[r + 2][columns].getContent())) {
                if (r + 3 < columnNumber && StringUtils.isNotBlank(boards[r + 3][columns].getContent())) {
                    continue;
                }
                if (r > 0 && StringUtils.isNotBlank(boards[r - 1][columns].getContent())) {
                    continue;
                }
                if(currentCell.getRow() != r && currentCell.getRow() != r+1 && currentCell.getRow() != r+2){
                    continue;
                }
                if (otherSide.equals(boards[r][columns].getContent()) &&
                        currentSide.equals(boards[r + 1][columns].getContent()) &&
                        currentSide.equals(boards[r + 2][columns].getContent())) {
                    anyChessEaten = true;
                    boards[r][columns].setClickable(false);
                    boards[r][columns].setContent("");
                } else if (otherSide.equals(boards[r + 2][columns].getContent()) &&
                        currentSide.equals(boards[r + 1][columns].getContent()) &&
                        currentSide.equals(boards[r][columns].getContent())) {
                    anyChessEaten = true;
                    boards[r + 2][columns].setClickable(false);
                    boards[r + 2][columns].setContent("");
                }
            }
        }

        for (int i = 0; i < columnNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                CellDTO cellDTO = boards[i][j];
                cellDTO.setColor("#282c34");
                String content = cellDTO.getContent();
                if (StringUtils.isNotBlank(cellDTO.getContent()) && !content.equals(currentSide)) {
                    cellDTO.setClickable(true);
                } else {
                    cellDTO.setClickable(false);
                }
            }
        }
        boards[currentCell.getRow()][currentCell.getColumn()].setName(beforeCell.getName());
        boards[currentCell.getRow()][currentCell.getColumn()].setRow(currentCell.getRow());
        boards[currentCell.getRow()][currentCell.getColumn()].setColumn(currentCell.getColumn());

        boards[beforeCell.getRow()][beforeCell.getColumn()].setName("");

        return Pair.of(boards,anyChessEaten);
    }


    private Boolean isAdjacent(int clickedRow, int clickedColumn, int checkRow, int checkColumn) {
        if (checkColumn == clickedColumn) {
            return Math.abs(clickedRow - checkRow) == 1;
        } else if (checkRow == clickedRow) {
            return Math.abs(checkColumn - clickedColumn) == 1;
        }
        return false;
    }
}
