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
 * @CreateTime: 2024-06-08  15:24
 * @Description: TODO
 */
@Slf4j
@Component
public class OnOpenStatusStrategy extends GameStatusHandleStrategy {
    @Override
    public WebSocketResult gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        log.info("OnOpenStatusStrategy.gameStatusHandle.webSocketReqDTO:{}", JSON.toJSONString(webSocketReqDTO));
        if (Objects.nonNull(ctx)) {
            WebSocketHandler.useridChannelMap.put(webSocketReqDTO.getUserId(), ctx.channel());
            WebSocketHandler.channelIdUserIdMap.put(ctx.channel().id().asLongText(), webSocketReqDTO.getUserId());
        }
        UserDTO userDTO = JSON.parseObject(redisClient.getValueByKey(webSocketReqDTO.getUserId()), UserDTO.class);
        //判断之前是否处于联机中,如果处于联机 判断对方是否还在线,如果对方不在线则返回给对方并且刷新游戏
        if (Objects.nonNull(userDTO)) {
            if (GameTypeEnum.INVITE.name().equals(userDTO.getGameType()) && StringUtils.isNotBlank(userDTO.getOtherUserId())) {
                String otherUserId = userDTO.getOtherUserId();
                UserDTO otherUserDTO = JSON.parseObject(redisClient.getValueByKey(otherUserId), UserDTO.class);
                if (Objects.nonNull(otherUserDTO) && GameTypeEnum.INVITE.name().equals(otherUserDTO.getGameType()) && userDTO.getUserId().equals(otherUserDTO.getOtherUserId())) {
                    //说明对方已经离线或在游戏中
                } else {
                    WebSocketHandler.broadcastMessage(userDTO.getUserId(), JSON.toJSONString(WebSocketResult.failure("",
                            WebSocketMessageType.NEED_REFRESH)));
                }
            }else{
                WebSocketHandler.userSet.add(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, userDTO.getUserName(), null, null, null, null));
                //说明是建立连接了
                redisClient.addValue(webSocketReqDTO.getUserId(), JSON.toJSONString(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, userDTO.getUserName(), null, null, null, null)));
            }
        } else {
            WebSocketHandler.userSet.add(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, webSocketReqDTO.getUserName(), null, null, null, null));
            //说明是建立连接了
            redisClient.addValue(webSocketReqDTO.getUserId(), JSON.toJSONString(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, webSocketReqDTO.getUserName(), null, null, null, null)));
        }
        return null;
    }

    @Override
    public boolean accept(WebSocketReqDTO webSocketReqDTO) {
        return GameStatusEnum.ON_OPEN.equals(webSocketReqDTO.getGameStatus());
    }
}
