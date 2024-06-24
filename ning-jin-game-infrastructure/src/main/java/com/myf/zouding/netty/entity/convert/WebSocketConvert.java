package com.myf.zouding.netty.entity.convert;

import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.entity.WebSocketEntity;
import com.myf.zouding.netty.entity.WebSocketReqDTO;

/**
 * @Author: myf
 * @CreateTime: 2024-06-08  14:23
 * @Description: WebSocketConvert
 */
public class WebSocketConvert {

    public static WebSocketReqDTO convertToDTO(WebSocketEntity entity) {
        if (entity == null) {
            return new WebSocketReqDTO();
        }
        WebSocketReqDTO webSocketReqDTO = new WebSocketReqDTO();
        webSocketReqDTO.setColumnNumber(entity.getColumnNumber());
        webSocketReqDTO.setGameStatus(GameStatusEnum.fromName(entity.getType()));
        webSocketReqDTO.setUserId(entity.getUserId());
        webSocketReqDTO.setFriendUserId(entity.getFriendUserId());
        webSocketReqDTO.setOtherUserId(entity.getOtherUserId());
        webSocketReqDTO.setUserName(entity.getUserName());
        return webSocketReqDTO;
    }
}
