package com.myf.zouding.service.impl;

import com.myf.zouding.netty.handler.GameStatusHandle;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import com.myf.zouding.service.GameStatusHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  18:01
 * @Description: GameStatusHandleServiceImpl
 */
@Service
public class GameStatusHandleServiceImpl implements GameStatusHandleService {

    @Autowired
    private GameStatusHandle gameStatusHandle;
    @Override
    public WebSocketResult gameStatusHandle(WebSocketReqDTO webSocketReqDTO) {
        return gameStatusHandle.statusHandle(webSocketReqDTO,null);
    }
}
