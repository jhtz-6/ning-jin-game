package com.myf.domain.zouding.service.statushandle.strategy.service;

import com.alibaba.fastjson.JSON;
import com.myf.common.enmus.UserStatusEnum;
import com.myf.common.enmus.WebSocketMessageType;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.res.Result;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.handler.WebSocketHandler;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-06-08  15:29
 * @Description: TODO
 */
@Slf4j
@Component
public class MatchStatusStrategy extends GameStatusHandleStrategy {
    @Override
    public WebSocketResult gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        if (WebSocketHandler.userSet.size() < 10) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(WebSocketResult.failure("当前在线人数较少,请选择邀请好友模式",
                    WebSocketMessageType.MATCH_GAME))));
            return null;
        }
        //获取到了玩家userId
        String userId = webSocketReqDTO.getUserId();
        //需要先把自己的状态改成匹配中
        UserDTO currentUserDTO = JSON.parseObject(redisClient.getValueByKey(userId), UserDTO.class);
        currentUserDTO.setUserStatusEnum(UserStatusEnum.MATCHING);
        redisClient.addValue(userId, JSON.toJSONString(currentUserDTO));
        currentUserDTO = matchGame(userId, currentUserDTO);
        WebSocketHandler.useridChannelMap.put(currentUserDTO.getUserId(), ctx.channel());
        // 响应消息给客户端 对手和自己匹配成功信息返回给前端
        Result<UserDTO> matchSuccessResult = WebSocketResult.success(JSON.parseObject(
                redisClient.getValueByKey(userId), UserDTO.class
        ), WebSocketMessageType.MATCH_GAME);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(matchSuccessResult)));
        return null;
    }

    @Override
    public boolean accept(WebSocketReqDTO webSocketReqDTO) {
        return GameStatusEnum.MATCH.equals(webSocketReqDTO.getGameStatus());
    }

    private synchronized UserDTO matchGame(String userId, UserDTO currentUserDTO) {
        currentUserDTO = JSON.parseObject(redisClient.getValueByKey(currentUserDTO.getUserId()), UserDTO.class);
        if (currentUserDTO.getUserStatusEnum().equals(UserStatusEnum.GAMEING)) {
            return currentUserDTO;
        }
        UserDTO otherUserDTO = null;
        while (Objects.isNull(otherUserDTO)) {
            for (UserDTO userDTO : WebSocketHandler.userSet) {
                if (!userDTO.getUserId().equals(userId)) {
                    String otherUserDTOJson = redisClient.getValueByKey(userDTO.getUserId());
                    if (StringUtils.isNotBlank(otherUserDTOJson)) {
                        otherUserDTO = JSON.parseObject(otherUserDTOJson, UserDTO.class);
                        if (!otherUserDTO.getUserStatusEnum().equals(UserStatusEnum.MATCHING)) {
                            otherUserDTO = null;
                        }
                    }
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        otherUserDTO.setUserStatusEnum(UserStatusEnum.GAMEING);
        otherUserDTO.setCell("X");
        otherUserDTO.setOtherUserName(currentUserDTO.getUserName());
        otherUserDTO.setOtherCell("O");
        otherUserDTO.setOtherUserId(currentUserDTO.getUserId());
        redisClient.addValue(otherUserDTO.getUserId(), JSON.toJSONString(otherUserDTO));

        currentUserDTO.setUserStatusEnum(UserStatusEnum.GAMEING);
        currentUserDTO.setCell("O");
        currentUserDTO.setOtherCell("X");
        currentUserDTO.setOtherUserId(otherUserDTO.getUserId());
        currentUserDTO.setOtherUserName(otherUserDTO.getUserName());
        redisClient.addValue(currentUserDTO.getUserId(), JSON.toJSONString(currentUserDTO));
        log.info("matchGame.currentUserDTO:{},otherUserDTO:{}", JSON.toJSONString(currentUserDTO), JSON.toJSONString(otherUserDTO));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return currentUserDTO;
    }
}
