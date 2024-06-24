package com.myf.domain.zouding.service.statushandle.strategy.service;

import com.alibaba.fastjson.JSON;
import com.myf.common.constant.BoardConstant;
import com.myf.common.enmus.GameTypeEnum;
import com.myf.common.enmus.UserStatusEnum;
import com.myf.common.enmus.WebSocketMessageType;
import com.myf.hleper.model.res.ResultErrorEnum;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.handler.WebSocketHandler;
import com.myf.zouding.netty.strategy.GameStatusHandleStrategy;
import com.myf.hleper.model.dto.CellDTO;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @Author: myf
 * @CreateTime: 2024-05-18  18:16
 * @Description: appointInviteStatusStrategy
 */
@Slf4j
@Component
public class AppointInviteStatusStrategy extends GameStatusHandleStrategy<Object> {

    @Override
    public WebSocketResult<Object> gameStatusHandle(WebSocketReqDTO webSocketReqDTO, ChannelHandlerContext ctx) {
        //被邀请者接受邀请,所以是被邀请者进入到了这里
        UserDTO invitedUser = webSocketReqDTO.getInvitedUser();
        UserDTO inviterUser = webSocketReqDTO.getInviterUser();
        String inviterUserId = inviterUser.getUserId();
        String userName = inviterUser.getUserName();
        Integer columnNumber = inviterUser.getColumnNumber();
        String invitedUserId = invitedUser.getUserId();
        UserDTO redisInvitedUser = JSON.parseObject(redisClient.getValueByKey(invitedUserId), UserDTO.class);
        //被邀请者刚进入
        if(Objects.isNull(redisInvitedUser)){
            //这里不设置管道,发消息的时候重试兼容这里
            UserDTO userDTO = new UserDTO(invitedUserId, UserStatusEnum.NEW, userName, null, null, null, null);
            userDTO.setAvatarUrl(invitedUser.getAvatarUrl());
            WebSocketHandler.userSet.add(userDTO);
            //说明是建立连接了
            redisClient.addValue(invitedUserId, JSON.toJSONString(userDTO));
        }
        //说明邀请者离线了
        UserDTO redisInviterUser = JSON.parseObject(redisClient.getValueByKey(inviterUserId), UserDTO.class);
        if(Objects.isNull(redisInviterUser)){
            //返回信息,对方离线
            WebSocketResult<Object> webSocketResult = WebSocketResult.failure(ResultErrorEnum.invite_fail_off_line.getMessage(), WebSocketMessageType.INVITE_FRIEND);
            WebSocketHandler.broadcastMessage(invitedUserId,JSON.toJSONString(webSocketResult));
            return webSocketResult;
        }
        if(UserStatusEnum.GAMEING.equals(redisInviterUser.getUserStatusEnum())){
            //返回信息,邀请者正在游戏中   发消息给被邀请者
            WebSocketResult<Object> webSocketResult = WebSocketResult.failure(ResultErrorEnum.invite_fail_gaming.getMessage(), WebSocketMessageType.INVITE_FRIEND);
            WebSocketHandler.broadcastMessage(invitedUserId,JSON.toJSONString(webSocketResult));
            return webSocketResult;
        }
        //设置邀请者
        inviterUser.setUserStatusEnum(UserStatusEnum.GAMEING);
        inviterUser.setCell(BoardConstant.board_x);
        inviterUser.setOtherUserName(invitedUser.getUserName());
        inviterUser.setOtherCell(BoardConstant.board_o);
        inviterUser.setOtherUserId(invitedUser.getUserId());
        inviterUser.setInitBoards(CellDTO.getXBoards(columnNumber));
        inviterUser.setOtherAvatarUrl(invitedUser.getAvatarUrl());
        inviterUser.setGameType(GameTypeEnum.INVITE.name());
        inviterUser.setInviteType(BoardConstant.INVITER);
        inviterUser.setColumnNumber(columnNumber);

        //设置被邀请者
        invitedUser.setUserStatusEnum(UserStatusEnum.GAMEING);
        invitedUser.setCell(BoardConstant.board_o);
        invitedUser.setOtherCell(BoardConstant.board_x);
        invitedUser.setOtherUserId(inviterUser.getUserId());
        invitedUser.setOtherUserName(inviterUser.getUserName());
        invitedUser.setInitBoards(CellDTO.getOBoards(columnNumber));
        invitedUser.setOtherAvatarUrl(inviterUser.getAvatarUrl());
        invitedUser.setGameType(GameTypeEnum.INVITE.name());
        invitedUser.setInviteType(BoardConstant.INVITED);
        invitedUser.setColumnNumber(columnNumber);
        log.info("AppointInviteStatusStrategy.gameStatusHandle.inviterUser:{},invitedUser:{}", JSON.toJSONString(inviterUser),JSON.toJSONString(invitedUser));
        redisClient.addValueToRedis(inviterUserId,JSON.toJSONString(inviterUser),1L, TimeUnit.DAYS);
        redisClient.addValueToRedis(invitedUserId,JSON.toJSONString(invitedUser),1L, TimeUnit.DAYS);
        // 响应消息给客户端 对手和自己匹配成功信息返回给前端
        WebSocketHandler.broadcastMessage(inviterUserId,JSON.toJSONString(WebSocketResult.success(inviterUser, WebSocketMessageType.MATCH_GAME)));
        WebSocketHandler.broadcastMessage(invitedUserId,JSON.toJSONString(WebSocketResult.success(invitedUser, WebSocketMessageType.MATCH_GAME)));
        return WebSocketResult.success(true,WebSocketMessageType.MATCH_GAME);
    }

    @Override
    public boolean accept(WebSocketReqDTO webSocketReqDTO) {
        return GameStatusEnum.APPOINT_INVITE.equals(webSocketReqDTO.getGameStatus());
    }
}
