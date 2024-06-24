package com.myf.zouding.netty.strategy;

import com.myf.hleper.client.RedisClient;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.database.handler.ApplicationContextUtil;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  18:14
 * @Description: GameStatusHandleStrategy
 */
@Component
public abstract class GameStatusHandleStrategy<T> {

    @Autowired
    protected RedisClient redisClient;


    public abstract WebSocketResult<T> gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx);


    public abstract boolean accept(WebSocketReqDTO webSocketReqDTO);
}
