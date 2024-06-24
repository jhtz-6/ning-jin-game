package com.myf.zouding.model.res;

import com.myf.hleper.model.dto.CellDTO;
import com.myf.hleper.model.dto.UserDTO;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-12-13  20:37
 * @Description: TODO
 */
@Data
public class HandleCellClickResult {

    /**
     * 是否结束
     */
    private Boolean isEnd;

    /**
     * 胜利者
     */
    private String winner;

    /**
     * 能够移动的位置
     */
    private CellDTO[][] canPlacePosition =new CellDTO[4][4];

    /**
     * 是否有棋子被吃掉
     */
    private Boolean anyChessEaten;

    /**
     * 阶数
     */
    private Integer columnNumber;

    /**
     * 对方还是己方
     */
    private String userType;


    private UserDTO userDTO;



}
