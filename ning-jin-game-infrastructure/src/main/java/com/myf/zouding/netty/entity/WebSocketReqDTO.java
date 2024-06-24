package com.myf.zouding.netty.entity;

import com.myf.hleper.model.dto.UserDTO;
import com.myf.zouding.netty.constant.GameStatusEnum;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-08  14:21
 * @Description: TODO
 */
@Data
public class WebSocketReqDTO {

    private Integer columnNumber;

    private GameStatusEnum gameStatus;

    private String userId;

    private String friendUserId;

    private String otherUserId;

    private String userName;

    private UserDTO inviterUser;

    private UserDTO invitedUser;
}
