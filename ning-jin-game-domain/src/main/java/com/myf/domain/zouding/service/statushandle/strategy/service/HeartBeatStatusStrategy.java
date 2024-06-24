package com.myf.domain.zouding.service.statushandle.strategy.service;

import com.alibaba.fastjson.JSON;
import com.myf.common.enmus.UserStatusEnum;
import com.myf.common.enmus.WebSocketMessageType;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import com.myf.zouding.netty.handler.WebSocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-06-08  14:35
 * @Description: HeartBeatStatusStrategy
 */
@Component
public class HeartBeatStatusStrategy extends GameStatusHandleStrategy<Integer> {


    @Override
    public WebSocketResult<Integer> gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        if(StringUtils.isNotBlank(webSocketReqDTO.getUserId()) && (Objects.isNull(WebSocketHandler.useridChannelMap.get(webSocketReqDTO.getUserId()))
                || !WebSocketHandler.useridChannelMap.get(webSocketReqDTO.getUserId()).isActive())){
            WebSocketHandler.useridChannelMap.put(webSocketReqDTO.getUserId(),ctx.channel());
            WebSocketHandler.channelIdUserIdMap.put(ctx.channel().id().asLongText(),webSocketReqDTO.getUserId());
            WebSocketHandler.userSet.add(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, webSocketReqDTO.getUserName(),null,null,null,null));
            //说明是建立连接了
            redisClient.addValue(webSocketReqDTO.getUserId(), JSON.toJSONString(new UserDTO(webSocketReqDTO.getUserId(), UserStatusEnum.NEW, webSocketReqDTO.getUserName(),null,null,null,null)));
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(WebSocketResult.success(WebSocketHandler.userSet.size(), WebSocketMessageType.HEART_BEAT))));
        return WebSocketResult.success(WebSocketHandler.userSet.size(), WebSocketMessageType.HEART_BEAT);
    }

    @Override
    public boolean accept(WebSocketReqDTO webSocketReqDTO) {
        return GameStatusEnum.HEART_BEAT.equals(webSocketReqDTO.getGameStatus());
    }
}
