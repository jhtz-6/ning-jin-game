package com.myf.domain.zouding.service.statushandle.strategy.service;

import com.alibaba.fastjson.JSON;
import com.myf.common.enmus.GameTypeEnum;
import com.myf.common.enmus.UserStatusEnum;
import com.myf.common.enmus.WebSocketMessageType;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import com.myf.zouding.netty.handler.WebSocketHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-06-08  15:26
 * @Description: TODO
 */
@Slf4j
@Component
public class OnCloseStatusStrategy extends GameStatusHandleStrategy {
    @Override
    public WebSocketResult gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        log.info("OnCloseStatusStrategy.gameStatusHandle:{},USER_SETS:{}",JSON.toJSONString(webSocketReqDTO), JSON.toJSONString(WebSocketHandler.userSet));
        UserDTO userDTO = JSON.parseObject(redisClient.getValueByKey(webSocketReqDTO.getUserId()), UserDTO.class);
        redisClient.deleteValueByKey(webSocketReqDTO.getUserId());
        WebSocketHandler.userSet.remove(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, userDTO.getUserName(),null,null,null,null));
        WebSocketHandler.useridChannelMap.remove(webSocketReqDTO.getUserId());
        //判断是否在联机中
        if(StringUtils.isNotBlank(userDTO.getOtherUserId()) && GameTypeEnum.INVITE.name().equals(userDTO.getGameType())){
            //说明是邀请 处于联机中 需要提示对方 我已下线。
            WebSocketHandler.broadcastMessage(userDTO.getOtherUserId(),JSON.toJSONString(WebSocketResult.failure("对方因惧怕您的实力,已下线,请点击右上角重新进入游戏",
                    WebSocketMessageType.INVITE_FRIEND)));
        }
        return null;
    }

    @Override
    public boolean accept(WebSocketReqDTO webSocketReqDTO) {
        return GameStatusEnum.ON_CLOSE.equals(webSocketReqDTO.getGameStatus());
    }
}
