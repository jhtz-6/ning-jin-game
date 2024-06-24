package com.myf.common.enmus;

/**
 * @author myf
 */

public enum WebSocketMessageType {

    BOARD("BOARD","棋盘数据"),
    CHANGE_COLUMN("CHANGE_COLUMN","更改阶数"),

    MATCH_GAME("MATCH_GAME","匹配游戏"),
    STAND_ALONE("STAND_ALONE","单机"),
    CELL_CLICK("CELL_CLICK","点击棋子"),
    INVITE_FRIEND("INVITE_FRIEND","邀请好友"),
    NEED_REFRESH("REFRESH","刷喜游戏"),
    HEART_BEAT("HEART_BEAT","邀请好友");


    private String name;

    private String value;

    WebSocketMessageType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
