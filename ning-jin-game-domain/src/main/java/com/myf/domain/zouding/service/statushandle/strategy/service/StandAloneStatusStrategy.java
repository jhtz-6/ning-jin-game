package com.myf.domain.zouding.service.statushandle.strategy.service;

import com.alibaba.fastjson.JSON;
import com.myf.common.enmus.GameTypeEnum;
import com.myf.common.enmus.UserStatusEnum;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-06-08  15:28
 * @Description: StandAloneStatusStrategy
 */
@Slf4j
@Component
public class StandAloneStatusStrategy extends GameStatusHandleStrategy {
    @Override
    public WebSocketResult gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        log.info("StandAloneStatusStrategy.gameStatusHandle.webSocketReqDTO:{}", JSON.toJSONString(webSocketReqDTO));
        String userId = webSocketReqDTO.getUserId();
        UserDTO currentUserDTO = JSON.parseObject(redisClient.getValueByKey(userId), UserDTO.class);
        if (Objects.isNull(currentUserDTO)) {
            currentUserDTO = new UserDTO();
            currentUserDTO.setUserId(userId);
        }
        if (!UserStatusEnum.GAMEING.equals(currentUserDTO.getUserStatusEnum())) {
            currentUserDTO.setUserStatusEnum(UserStatusEnum.GAMEING);
            currentUserDTO.setGameType(GameTypeEnum.SINGLE.name());
            redisClient.addValue(userId, JSON.toJSONString(currentUserDTO));
        }
        return null;
    }

    @Override
    public boolean accept(WebSocketReqDTO webSocketReqDTO) {
        return GameStatusEnum.STAND_ALONE.equals(webSocketReqDTO.getGameStatus());
    }
}
