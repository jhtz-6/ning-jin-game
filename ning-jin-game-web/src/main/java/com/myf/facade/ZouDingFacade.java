package com.myf.facade;

import com.alibaba.fastjson.JSON;
import com.myf.common.enmus.GameTypeEnum;
import com.myf.common.enmus.WebSocketMessageType;
import com.myf.common.model.RequestLogDTO;
import com.myf.common.util.RequestLogUtils;

import com.myf.hleper.model.dto.*;
import com.myf.hleper.model.res.Result;
import com.myf.hleper.model.res.ResultErrorEnum;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.model.res.AnonymousIdentityResult;
import com.myf.zouding.model.res.HandleCellClickResult;
import com.myf.zouding.netty.handler.WebSocketHandler;
import com.myf.zouding.service.WxApiHandlerService;
import com.myf.zouding.service.ZouDingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: myf
 * @CreateTime: 2023-12-12  21:58
 * @Description: ZouDingFacade
 */
@RestController
@RequestMapping("zouDing")
public class ZouDingFacade {


    @Resource
    ZouDingService zouDingService;

    @Resource
    ZouDingService wxZouDingService;

    @Autowired
    WxApiHandlerService wxApiHandlerService;

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                modelAndView.addObject(cookie.getName(), cookie.getValue());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("userId"))) {
            modelAndView.addObject("inviteOtherUserId", request.getParameter("userId"));
        }
        if (StringUtils.isNotBlank(request.getParameter("columnNumber"))) {
            modelAndView.addObject("columnNumber", request.getParameter("columnNumber"));
        }
        System.out.println("inviteOtherUserId:" + request.getParameter("userId") + ";columnNumber" + request.getParameter("columnNumber"));
        modelAndView.setViewName("page/index");
        return modelAndView;
    }


    @GetMapping("/wxLogin")
    public Result<WxLoginDTO> wxLogin(String jsCode) {
        return wxApiHandlerService.loginByJsCode(jsCode);
    }


    @Deprecated
    @PostMapping("/handleCellClick")
    public Result<HandleCellClickResult> handleCellClick(@RequestBody BoardDTO boardDTO) {
        Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> resultResultPair = zouDingService.handleCellClick(boardDTO);
        WebSocketHandler.broadcastMessage(boardDTO.getUserId(), JSON.toJSONString(resultResultPair.getLeft()));
        //构建对方棋盘,镜像
        WebSocketHandler.broadcastMessage(boardDTO.getOtherUserId(), JSON.toJSONString(resultResultPair.getRight()));
        return resultResultPair.getLeft();
    }

    @PostMapping("/wxHandleCellClick")
    public Result<HandleCellClickResult> wxHandleCellClick(@RequestBody BoardDTO boardDTO) {
        Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> resultResultPair = wxZouDingService.handleCellClick(boardDTO);
        WebSocketHandler.broadcastMessage(boardDTO.getUserId(), JSON.toJSONString(resultResultPair.getLeft()));
        //构建对方棋盘,镜像 单机游戏 不发送
        if (!GameTypeEnum.SINGLE.name().equals(boardDTO.getGameType())) {
            WebSocketHandler.broadcastMessage(boardDTO.getOtherUserId(), JSON.toJSONString(resultResultPair.getRight()));
        }
        return null;
    }

    @Deprecated
    @GetMapping("/generateAnonymousId")
    public Result<AnonymousIdentityResult> generateAnonymousId(HttpServletRequest request) {
        // 生成匿名标识，可以使用 UUID 或其他方法
        String anonymousId = UUID.randomUUID().toString();
        AnonymousIdentityResult anonymousIdentityResult = new AnonymousIdentityResult();
        anonymousIdentityResult.setUserId(anonymousId);
        // 可以将生成的匿名标识存储在数据库中，以便后续使用
        RequestLogDTO requestLogDTO = RequestLogUtils.buildRequestLogByHttpRequest(request);
        if (Objects.nonNull(requestLogDTO)) {
            anonymousIdentityResult.setPosition(requestLogDTO.getLocation());
            anonymousIdentityResult.setName(requestLogDTO.getUserName());
            anonymousIdentityResult.setIp(requestLogDTO.getIpAddress());
        }
        return Result.success(anonymousIdentityResult);
    }

    @Deprecated
    @GetMapping("/appointInvite")
    public void appointInvite(String userId, Integer columnNumber, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("userId", userId);
            request.setAttribute("columnNumber", columnNumber);
            request.getRequestDispatcher("/zouDing/index").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成邀请码
     *
     * @param userId
     * @param level
     * @return
     */
    @GetMapping("/generateInvitationCode")
    public Result<String> generateInvitationCode(String userId, Integer level) {
        if (StringUtils.isBlank(userId) || Objects.isNull(level)) {
            return Result.failure("userId and level cannot be null");
        }
        return wxZouDingService.generateInvitationCode(userId, level);
    }

    /**
     * 点击邀请链接
     *
     * @param invitedId
     * @param invitationCode
     */
    @GetMapping("/clickInvitation")
    public void clickInvitation(String invitedId, String invitationCode) {
        if (StringUtils.isBlank(invitationCode) || Objects.isNull(invitedId)) {
            WebSocketHandler.broadcastMessage(invitedId, JSON.toJSONString(WebSocketResult.failure(
                    ResultErrorEnum.invitation_expired.getMessage(), WebSocketMessageType.MATCH_GAME)));
            return;
        }
        Result<String> result = wxZouDingService.clickInvitation(invitedId, invitationCode);
        if (!result.isSuccess()) {
            WebSocketHandler.broadcastMessage(invitedId, JSON.toJSONString(WebSocketResult.failure(result.getErrorMsg(), WebSocketMessageType.MATCH_GAME)));
        }
    }


    /**
     * 微信小游戏注册和登陆
     *
     * @param wechatRegisterAndLoginDTO
     * @return
     */
    @PostMapping("/wechatRegisterAndLogin")
    public Result<UserDTO> wechatRegisterAndLogin(@RequestBody WechatRegisterAndLoginDTO wechatRegisterAndLoginDTO) {
        return wxZouDingService.wechatRegisterAndLogin(wechatRegisterAndLoginDTO);
    }

    /**
     * 更改游戏阶数
     *
     * @param boardDTO
     * @return
     */
    @PostMapping("/changeColumnNumber")
    public void changeColumnNumber(@RequestBody BoardDTO boardDTO) {
        Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> resultResultPair = wxZouDingService.changeColumnNumber(boardDTO);
        WebSocketHandler.broadcastMessage(boardDTO.getUserId(), JSON.toJSONString(WebSocketResult.success(resultResultPair.getLeft().getData()
                , WebSocketMessageType.CHANGE_COLUMN)));
        //构建对方棋盘,镜像 单机游戏 不发送
        if (!GameTypeEnum.SINGLE.name().equals(boardDTO.getGameType())) {
            WebSocketHandler.broadcastMessage(boardDTO.getOtherUserId(),
                    JSON.toJSONString(WebSocketResult.success(resultResultPair.getRight().getData(), WebSocketMessageType.CHANGE_COLUMN)));
        }
    }

    /**
     * websocket链接关闭
     *
     * @param userId
     * @param userName
     * @return
     */
    @Deprecated
    @GetMapping("/webSocketClose")
    public Result<String> webSocketClose(String userId, String userName) {
        if (StringUtils.isBlank(userId) || Objects.isNull(userName)) {
            return Result.failure("userId and userName cannot be null");
        }
        return wxZouDingService.webSocketClose(userId, userName);
    }

    @GetMapping("/getRankingList")
    public Result<List<RankingDTO>> getRankingList() {
        return wxZouDingService.getRankingList();
    }

    @GetMapping("/feedBack")
    public Result<Boolean> feedBack(String userId, String userName,String content, String subject) {
        if (StringUtils.isAnyBlank(content, subject,userId,userName)) {
            return Result.failure("param cannot be null");
        }
        return wxZouDingService.feedBack(userId,userName,content, subject);
    }
}
