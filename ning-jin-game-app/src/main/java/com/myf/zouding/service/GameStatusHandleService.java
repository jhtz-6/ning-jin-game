package com.myf.zouding.service;

import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;

/**
 * @author myf
 */
public interface GameStatusHandleService {

    WebSocketResult gameStatusHandle(WebSocketReqDTO webSocketReqDTO);
}
