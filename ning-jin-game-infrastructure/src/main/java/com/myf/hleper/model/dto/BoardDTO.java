package com.myf.hleper.model.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-12-13  19:45
 *@Description: TODO
 */
@Data
public class BoardDTO {

    private CellDTO[][] boards =new CellDTO[4][4];

    private Integer row;

    private Integer column;

    private CellDTO currentCell;

    private CellDTO beforeCell;

    private String otherUserId;

    private String otherUserName;

    private String userId;

    private String userName;

    private Integer columnNumber;

    private String gameType;
    private String inviteType;

}
