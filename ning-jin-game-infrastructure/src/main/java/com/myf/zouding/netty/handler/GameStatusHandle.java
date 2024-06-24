package com.myf.zouding.netty.handler;

import com.alibaba.fastjson.JSON;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  18:45
 * @Description: GameStatusHandle
 */
@Slf4j
@Component
public class GameStatusHandle {

    @Autowired
    private List<GameStatusHandleStrategy> strategies;


    public WebSocketResult statusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        for (GameStatusHandleStrategy strategy : strategies) {
            if (strategy.accept(webSocketReqDTO)) {
                return strategy.gameStatusHandle(webSocketReqDTO, ctx);
            }
        }
        throw new UnsupportedOperationException("No suitable strategy found for handling the game status.");
    }
}
