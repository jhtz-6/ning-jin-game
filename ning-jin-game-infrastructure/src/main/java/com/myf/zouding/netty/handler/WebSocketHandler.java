package com.myf.zouding.netty.handler;

import com.alibaba.fastjson.JSON;

import com.myf.common.util.RetryUtils;
import com.myf.hleper.client.RedisClient;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.zouding.database.handler.ApplicationContextUtil;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.entity.WebSocketEntity;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import com.myf.zouding.netty.entity.convert.WebSocketConvert;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * @Author: myf
 * @CreateTime: 2023-12-16  14:49
 * @Description: ServerHandler
 */
@Slf4j
@Component
public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    private WebSocketServerHandshaker handshaker;

    static final RedisClient redisClient = (RedisClient) ApplicationContextUtil.getBeanByName("redisClient");
    static final GameStatusHandle gameStatusHandle = (GameStatusHandle) ApplicationContextUtil.getBeanByName("gameStatusHandle");


    /**
     * 后面换成bean来处理 todo
     */
    public static Map<String, Channel> useridChannelMap = new HashMap<>();

    /**
     * 后面换成bean来处理 todo
     */
    public static Map<String, String> channelIdUserIdMap = new HashMap<>();

    /**
     * 存储所有的在线玩家 后面换成bean来处理 todo
     */
    public static Set<UserDTO> userSet = new HashSet<>();


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("WebSocketHandler.exceptionCaught.caught", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("WebSocketHandler.channelActive");
    }

    /**
     * 链接断开
     * 1、需要从 channelIdUserIdMap 中把channel剔除
     * 2、如果在游戏中,还需要通知对方 已断线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("WebSocketHandler.channelInactive.channelIdUserIdMap:{}",JSON.toJSONString(channelIdUserIdMap));
        String userId = channelIdUserIdMap.get(ctx.channel().id().asLongText());
        if(StringUtils.isNotBlank(userId)){
            WebSocketReqDTO webSocketReqDTO =new WebSocketReqDTO();
            webSocketReqDTO.setUserId(userId);
            webSocketReqDTO.setGameStatus(GameStatusEnum.ON_CLOSE);
            gameStatusHandle.statusHandle(webSocketReqDTO, ctx);
            channelIdUserIdMap.remove(ctx.channel().id().asLongText());
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 检查是否建立 WebSocket 连接 wss://www.520myf.com:8084/websocket -> ws://localhost:8084/websocket
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "wss://www.520myf.com:8084/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 处理 WebSocket 消息
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        } else if (frame instanceof TextWebSocketFrame) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            // 处理文本消息
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String message = textFrame.text();
            try {
                if (StringUtils.isBlank(message)) {
                    return;
                }
                WebSocketEntity socketEntity = JSON.parseObject(message, WebSocketEntity.class);
                if(StringUtils.isBlank(socketEntity.getUserId())){
                    return;
                }
                gameStatusHandle.statusHandle(WebSocketConvert.convertToDTO(socketEntity), ctx);
                // 响应消息给客户端
            } catch (Exception e) {
                log.error("handleWebSocketFrame.message:{}",message,e);
                ctx.channel().writeAndFlush(new TextWebSocketFrame("received error message:" + message));
            }
        } else if (frame instanceof PingWebSocketFrame) {
            // 处理 Ping 消息
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
        } else {
            // 不支持的消息类型
            throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
        }
    }

    /**
     * 向指定的客户端发送消息
     *
     * @param userId
     * @param message
     */
    public static void broadcastMessage(String userId, String message) {
        log.info("broadcastMessage.CHANNEL_MAP:{}", JSON.toJSONString(useridChannelMap));
        if (StringUtils.isBlank(userId) || Objects.isNull(useridChannelMap.get(userId))) {
            return;
        }
        RetryUtils.retry(5,1000,() -> useridChannelMap.get(userId), Objects::nonNull)
                .writeAndFlush(new TextWebSocketFrame(message));
    }
}
