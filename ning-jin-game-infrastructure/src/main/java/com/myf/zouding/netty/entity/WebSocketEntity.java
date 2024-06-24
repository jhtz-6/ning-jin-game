package com.myf.zouding.netty.entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-12-31  18:12
 * @Description: SocketEntity
 */
@Data
public class WebSocketEntity {

    private Integer columnNumber;

    private String type;

    private String userId;

    private String friendUserId;

    private String otherUserId;

    private String userName;
}
