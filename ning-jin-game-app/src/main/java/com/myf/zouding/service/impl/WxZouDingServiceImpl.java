package com.myf.zouding.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.myf.common.constant.BoardConstant;
import com.myf.common.constant.UserTypeConstant;
import com.myf.common.enmus.GameTypeEnum;
import com.myf.common.enmus.InvitationCodeStatus;
import com.myf.common.enmus.WebSocketMessageType;
import com.myf.common.util.BooleanLogicExecuteUtils;
import com.myf.domain.zouding.service.FiveChessDomainService;
import com.myf.domain.zouding.service.InvitationCodeDomainService;
import com.myf.hleper.client.TencentMailClient;
import com.myf.hleper.convert.RankConvert;
import com.myf.hleper.convert.UserConvert;
import com.myf.hleper.model.dto.*;
import com.myf.hleper.model.res.Result;
import com.myf.hleper.model.res.ResultErrorEnum;
import com.myf.hleper.model.res.WebSocketResult;
import com.myf.zouding.database.driver.*;
import com.myf.zouding.database.entity.FeedbackDO;
import com.myf.zouding.database.entity.GameResultDO;
import com.myf.zouding.database.entity.InvitationCodeDO;
import com.myf.zouding.database.entity.UserDO;
import com.myf.zouding.database.param.PlayerStatisticsQueryParam;
import com.myf.zouding.model.res.HandleCellClickResult;
import com.myf.zouding.netty.constant.GameStatusEnum;
import com.myf.zouding.netty.entity.WebSocketReqDTO;
import com.myf.zouding.service.GameStatusHandleService;
import com.myf.zouding.service.ZouDingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: myf
 * @CreateTime: 2024-05-02  19:48
 * @Description: TODO
 */
@Slf4j
@Service("wxZouDingService")
public class WxZouDingServiceImpl implements ZouDingService {

    private static final Map<Integer, Integer> BOARD_FOUR_MAP = new HashMap<>();
    private static final Map<Integer, Integer> BOARD_FIVE_MAP = new HashMap<>();
    private static final Map<Integer, Integer> BOARD_FIX_MAP = new HashMap<>();
    private static final Map<Integer, Map<Integer, Integer>> BOARD_MAP = new HashMap<>();

    static {
        BOARD_FOUR_MAP.put(0, 3);
        BOARD_FOUR_MAP.put(1, 2);
        BOARD_FOUR_MAP.put(2, 1);
        BOARD_FOUR_MAP.put(3, 0);

        BOARD_FIVE_MAP.put(0, 4);
        BOARD_FIVE_MAP.put(1, 3);
        BOARD_FIVE_MAP.put(2, 2);
        BOARD_FIVE_MAP.put(3, 1);
        BOARD_FIVE_MAP.put(4, 0);

        BOARD_FIX_MAP.put(0, 5);
        BOARD_FIX_MAP.put(1, 4);
        BOARD_FIX_MAP.put(2, 3);
        BOARD_FIX_MAP.put(3, 2);
        BOARD_FIX_MAP.put(4, 1);
        BOARD_FIX_MAP.put(5, 0);

        BOARD_MAP.put(4, BOARD_FOUR_MAP);
        BOARD_MAP.put(5, BOARD_FIVE_MAP);
        BOARD_MAP.put(6, BOARD_FIX_MAP);

    }

    @Autowired
    FiveChessDomainService wxFiveChessDomainService;
    @Autowired
    private InvitationCodeDomainService invitationCodeDomainService;
    @Autowired
    private InvitationCodeRepo invitationCodeRepo;
    @Autowired
    private GameResultRepo gameResultRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GameStatusHandleService gameStatusHandleService;

    @Autowired
    private PlayerStatisticsRepo playerStatisticsRepo;

    @Autowired
    private TencentMailClient tencentMailClient;

    @Autowired
    private FeedbackRepo feedbackRepo;


    @Override
    public Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> handleCellClick(BoardDTO boardDTO) {
        log.info("WxFiveChessServiceImpl.handleCellClick.boardDTO:{}", JSON.toJSONString(boardDTO));
        HandleCellClickResult handleCellClickResult = new HandleCellClickResult();
        HandleCellClickResult otherHandleCellClickResult = new HandleCellClickResult();
        handleCellClickResult.setUserType(UserTypeConstant.current);
        Boolean gameIsOver = false;
        otherHandleCellClickResult.setUserType(UserTypeConstant.other);
        Integer columnNumber = boardDTO.getColumnNumber();
        Map<Integer, Integer> boardMap = BOARD_MAP.get(columnNumber);
        if (wxFiveChessDomainService.isToMove(boardDTO)) {
            //需要判断哪些位置可以存放
            handleCellClickResult.setCanPlacePosition(wxFiveChessDomainService.canPlacePosition(boardDTO));
            CellDTO[][] canPlacePosition = handleCellClickResult.getCanPlacePosition();
            CellDTO[][] otherCanPlacePosition = new CellDTO[columnNumber][columnNumber];
            for (int i = 0; i < columnNumber; i++) {
                for (int j = 0; j < columnNumber; j++) {
                    otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)] = new CellDTO();
                    BeanUtils.copyProperties(canPlacePosition[i][j], otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)]);
                }
            }
            otherHandleCellClickResult.setCanPlacePosition(otherCanPlacePosition);
        } else {
            //需要是否结束
            Pair<CellDTO[][], Boolean> moveResult = wxFiveChessDomainService.moveToNewPosition(boardDTO);
            handleCellClickResult.setCanPlacePosition(moveResult.getLeft());
            handleCellClickResult.setAnyChessEaten(moveResult.getRight());
            otherHandleCellClickResult.setAnyChessEaten(moveResult.getRight());
            boardDTO.setBoards(handleCellClickResult.getCanPlacePosition());
            gameIsOver = wxFiveChessDomainService.gameIsOver(boardDTO);
            handleCellClickResult.setIsEnd(gameIsOver);
            otherHandleCellClickResult.setIsEnd(gameIsOver);
            if (gameIsOver) {
                handleCellClickResult.setWinner(boardDTO.getBeforeCell().getContent());
                otherHandleCellClickResult.setWinner(boardDTO.getBeforeCell().getContent());
                //设置游戏结束后的重置棋盘   始终是邀请者下X白上O黑
                if (GameTypeEnum.SINGLE.name().equals(boardDTO.getGameType())) {
                    //单机永远是下X白上O黑
                    handleCellClickResult.setCanPlacePosition(CellDTO.getXBoards(columnNumber));
                    otherHandleCellClickResult.setCanPlacePosition(CellDTO.getOBoards(columnNumber));
                } else {
                    if ("X".equals(boardDTO.getCurrentCell().getContent())) {
                        handleCellClickResult.setCanPlacePosition(CellDTO.getXBoards(columnNumber));
                        otherHandleCellClickResult.setCanPlacePosition(CellDTO.getOBoards(columnNumber));
                    } else {
                        handleCellClickResult.setCanPlacePosition(CellDTO.getOBoards(columnNumber));
                        otherHandleCellClickResult.setCanPlacePosition(CellDTO.getXBoards(columnNumber));
                    }
                }
                UserDTO inviter = new UserDTO();
                UserDTO invited = new UserDTO();
                inviter.setUserId(boardDTO.getUserId());
                invited.setUserId(boardDTO.getOtherUserId());
                buildWinAndLoseCount(inviter, invited);
                handleCellClickResult.setUserDTO(inviter);
                otherHandleCellClickResult.setUserDTO(invited);
            }
            CellDTO[][] canPlacePosition = handleCellClickResult.getCanPlacePosition();
            CellDTO[][] otherCanPlacePosition = new CellDTO[columnNumber][columnNumber];
            for (int i = 0; i < columnNumber; i++) {
                for (int j = 0; j < columnNumber; j++) {
                    otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)] = new CellDTO();
                    BeanUtils.copyProperties(canPlacePosition[i][j], otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)]);
                    if (StringUtils.isNotBlank(otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)].getContent()) &&
                            !otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)].getContent().equals(boardDTO.getCurrentCell().getContent())) {
                        otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)].setClickable(true);
                    } else {
                        otherCanPlacePosition[boardMap.get(i)][boardMap.get(j)].setClickable(false);
                    }
                }
            }
            otherHandleCellClickResult.setCanPlacePosition(otherCanPlacePosition);
        }
        //更改游戏状态 目前仅需要单机游戏需要更改
        if (GameTypeEnum.SINGLE.name().equals(boardDTO.getGameType())) {
            WebSocketReqDTO webSocketReqDTO = new WebSocketReqDTO();
            webSocketReqDTO.setUserId(boardDTO.getUserId());
            //单机状态改为open状态
            webSocketReqDTO.setGameStatus(gameIsOver ? GameStatusEnum.ON_OPEN : GameStatusEnum.STAND_ALONE);
            gameStatusHandleService.gameStatusHandle(webSocketReqDTO);
        }
        return Pair.of(WebSocketResult.success(handleCellClickResult, WebSocketMessageType.CELL_CLICK),
                WebSocketResult.success(otherHandleCellClickResult, WebSocketMessageType.CELL_CLICK));
    }

    @Override
    public Result<String> generateInvitationCode(String userId, Integer level) {
        log.info("WxFiveChessServiceImpl.generateInvitationCode.userId:{},level:{}", userId, level);
        if (StringUtils.isAnyBlank(userId) || Objects.isNull(level)) {
            return Result.failure("userId and level cannot be null");
        }
        Result<String> result = invitationCodeDomainService.generateInvitationCode();
        if (result.isSuccess() && StringUtils.isNotBlank(result.getData())) {
            //todo 改成DTO传输
            InvitationCodeDO invitationCodeDO = new InvitationCodeDO();
            invitationCodeDO.setInvitationCode(result.getData());
            invitationCodeDO.setInviterId(userId);
            //下面的状态改成枚举
            invitationCodeDO.setStatus(InvitationCodeStatus.PENDING.getCode());
            HashMap<String, String> map = Maps.newHashMap();
            map.put("userId", userId);
            map.put("level", String.valueOf(level));
            invitationCodeDO.setParam(JSON.toJSONString(map));
            invitationCodeRepo.save(invitationCodeDO);
        }
        return result;
    }

    @Override
    public Result<String> clickInvitation(String invitedId, String invitationCode) {
        log.info("WxFiveChessServiceImpl.clickInvitation.invitedId:{},invitationCode:{}", invitedId, invitationCode);
        //根据邀请码查询
        InvitationCodeDO invitationCodeDO = invitationCodeRepo.selectByInvitationCode(invitationCode);
        if (Objects.nonNull(invitationCodeDO) && InvitationCodeStatus.PENDING.getCode().equals(invitationCodeDO.getStatus())) {
            //再查询邀请者和被邀请者信息
            UserDTO inviterUser = UserConvert.DO2DTO(userRepo.selectByOpenId(invitationCodeDO.getInviterId()), invitationCodeDO);
            UserDTO invitedUser = UserConvert.DO2DTO(userRepo.selectByOpenId(invitedId), invitationCodeDO);
            buildWinAndLoseCount(inviterUser, invitedUser);
            WebSocketReqDTO webSocketReqDTO = new WebSocketReqDTO();
            webSocketReqDTO.setInvitedUser(invitedUser);
            webSocketReqDTO.setInviterUser(inviterUser);
            webSocketReqDTO.setGameStatus(GameStatusEnum.APPOINT_INVITE);
            WebSocketResult webSocketResult = gameStatusHandleService.gameStatusHandle(webSocketReqDTO);
            if (webSocketResult.isSuccess()) {
                //说明是待处理 改为已接受
                invitationCodeDO.setStatus(InvitationCodeStatus.ACCEPTED.getCode());
                invitationCodeRepo.updateById(invitationCodeDO);
            }
            return Result.success(invitationCodeDO.getInviterId());
        } else {
            //设置邀请已过期
            return Result.failure(ResultErrorEnum.invitation_expired.getMessage());
        }
    }

    private void buildWinAndLoseCount(UserDTO inviterUser, UserDTO invitedUser) {
        String inviterId = inviterUser.getUserId();
        String invitedId = invitedUser.getUserId();
        List<GameResultDO> gameWinResultList = gameResultRepo.selectWinCounts(Arrays.asList(inviterId, invitedId),
                GameTypeEnum.INVITE);
        String invitedWinCount = gameWinResultList.stream().filter(x -> invitedId.equals(x.getPlayer1Id())).map(GameResultDO::getCount).findFirst().orElse("0");
        String inviterWinCount = gameWinResultList.stream().filter(x -> inviterId.equals(x.getPlayer1Id())).map(GameResultDO::getCount).findFirst().orElse("0");
        inviterUser.setOtherWinCount(Integer.valueOf(invitedWinCount));
        invitedUser.setWinCount(Integer.valueOf(invitedWinCount));
        invitedUser.setOtherWinCount(Integer.valueOf(inviterWinCount));
        inviterUser.setWinCount(Integer.valueOf(inviterWinCount));
        List<GameResultDO> gameLoseResultList = gameResultRepo.selectLoseCounts(Arrays.asList(inviterId, invitedId),
                GameTypeEnum.INVITE);
        String invitedLoseCount = gameLoseResultList.stream().filter(x -> invitedId.equals(x.getPlayer2Id())).map(GameResultDO::getCount).findFirst().orElse("0");
        String inviterLoseCount = gameLoseResultList.stream().filter(x -> inviterId.equals(x.getPlayer2Id())).map(GameResultDO::getCount).findFirst().orElse("0");
        inviterUser.setOtherLoseCount(Integer.valueOf(invitedLoseCount));
        invitedUser.setLoseCount(Integer.valueOf(invitedLoseCount));
        invitedUser.setOtherLoseCount(Integer.valueOf(inviterLoseCount));
        inviterUser.setLoseCount(Integer.valueOf(inviterLoseCount));
    }


    @Override
    public Result<UserDTO> wechatRegisterAndLogin(WechatRegisterAndLoginDTO wechatRegisterAndLoginDTO) {
        log.error("WxFiveChessServiceImpl.wechatRegisterAndLogin.wechatRegisterAndLoginDTO:{}", JSON.toJSONString(wechatRegisterAndLoginDTO));
        String openId = wechatRegisterAndLoginDTO.getOpenId();
        if (StringUtils.isNotBlank(openId)) {
            UserDO userDO = userRepo.selectByOpenId(openId);
            BooleanLogicExecuteUtils.execute(
                    Objects.isNull(userDO),
                    () -> register(wechatRegisterAndLoginDTO),
                    () -> updateUser(userDO, wechatRegisterAndLoginDTO)
            );
            return Result.success(null);
        }
        return Result.success(null);
    }

    @Override
    public Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> changeColumnNumber(BoardDTO boardDTO) {
        log.info("WxFiveChessServiceImpl.changeColumnNumber.boardDTO:{}", JSON.toJSONString(boardDTO));
        HandleCellClickResult handleCellClickResult = new HandleCellClickResult();
        HandleCellClickResult otherHandleCellClickResult = new HandleCellClickResult();
        handleCellClickResult.setColumnNumber(boardDTO.getColumnNumber());
        otherHandleCellClickResult.setColumnNumber(boardDTO.getColumnNumber());
        //胜利方 是X还是O
        String content = boardDTO.getCurrentCell().getContent();
        if (BoardConstant.board_x.equalsIgnoreCase(content)) {
            //说明是X 那么更改后的棋盘还是X  下X上O
            handleCellClickResult.setCanPlacePosition(CellDTO.getXBoards(boardDTO.getColumnNumber()));
            otherHandleCellClickResult.setCanPlacePosition(CellDTO.getOBoards(boardDTO.getColumnNumber()));
        } else {
            handleCellClickResult.setCanPlacePosition(CellDTO.getOBoards(boardDTO.getColumnNumber()));
            otherHandleCellClickResult.setCanPlacePosition(CellDTO.getXBoards(boardDTO.getColumnNumber()));
        }
        log.info("WxFiveChessServiceImpl.changeColumnNumber.handleCellClickResult:{}", JSON.toJSONString(handleCellClickResult));
        log.info("WxFiveChessServiceImpl.changeColumnNumber.otherHandleCellClickResult:{}", JSON.toJSONString(otherHandleCellClickResult));
        return Pair.of(Result.success(handleCellClickResult), Result.success(otherHandleCellClickResult));
    }

    @Override
    public Result<String> webSocketClose(String userId, String userName) {
        WebSocketReqDTO webSocketReqDTO = new WebSocketReqDTO();
        webSocketReqDTO.setUserId(userId);
        webSocketReqDTO.setUserName(userName);
        webSocketReqDTO.setGameStatus(GameStatusEnum.ON_CLOSE);
        return gameStatusHandleService.gameStatusHandle(webSocketReqDTO);
    }


    private void updateUser(UserDO userDO, WechatRegisterAndLoginDTO wechatRegisterAndLoginDTO) {
        if (StringUtils.isNotBlank(wechatRegisterAndLoginDTO.getAvatarUrl())
                && !wechatRegisterAndLoginDTO.getAvatarUrl().equals(userDO.getAvatarUrl())) {
            userDO.setAvatarUrl(wechatRegisterAndLoginDTO.getAvatarUrl());
        }
        if (StringUtils.isNotBlank(wechatRegisterAndLoginDTO.getNickName()) &&
                !wechatRegisterAndLoginDTO.getNickName().equals(userDO.getNickName())) {
            userDO.setNickName(wechatRegisterAndLoginDTO.getNickName());
        }
        userDO.setLastLoginTime(new Date());
        userRepo.updateById(userDO);
    }

    private void register(WechatRegisterAndLoginDTO wechatRegisterAndLoginDTO) {
        UserDO userDO = new UserDO();
        userDO.setOpenId(wechatRegisterAndLoginDTO.getOpenId());
        userDO.setAvatarUrl(wechatRegisterAndLoginDTO.getAvatarUrl());
        userDO.setGender(wechatRegisterAndLoginDTO.getGender());
        userDO.setNickName(wechatRegisterAndLoginDTO.getNickName());
        userRepo.save(userDO);
    }

    @Override
    public Result<List<RankingDTO>> getRankingList() {
        return Result.success(RankConvert.DOList2RankingDTOList(playerStatisticsRepo.selectRankingListByParam(
                new PlayerStatisticsQueryParam())));
    }

    @Override
    public Result<Boolean> feedBack(String openId, String userName,String content,String subject) {
        //todo存入数据库表
        FeedbackDO feedbackDO =new FeedbackDO();
        feedbackDO.setContent(content);
        feedbackDO.setOpenId(openId);
        //1 youxiao
        feedbackDO.setEffective("1");
        feedbackDO.setSubject(subject);
        feedbackDO.setUserName(userName);
        feedbackRepo.save(feedbackDO);
        TencentMailDTO tencentMailDTO =new TencentMailDTO();
        tencentMailDTO.setText(String.format("openid是%s,昵称是%s,反馈的内容是%s",openId,userName,content));
        tencentMailDTO.setSubject(subject);
        return Result.success(tencentMailClient.sendMailToSelf(tencentMailDTO));
    }
}
