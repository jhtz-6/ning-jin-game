package com.myf.zouding.service;

import com.myf.hleper.model.dto.BoardDTO;
import com.myf.hleper.model.dto.RankingDTO;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.hleper.model.dto.WechatRegisterAndLoginDTO;
import com.myf.hleper.model.res.Result;
import com.myf.hleper.model.res.ResultErrorEnum;
import com.myf.zouding.model.res.HandleCellClickResult;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author myf
 */
public interface ZouDingService {

    /**
     * 处理棋盘点击事件
     *
     * @param boardDTO
     * @return left是己方结果, right是对方结果
     */
    Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> handleCellClick(BoardDTO boardDTO);

    /**
     * 根据userId和level获取邀请码
     *
     * @param userId
     * @param level
     * @return
     */
    default Result<String> generateInvitationCode(String userId, Integer level) {
        return Result.failure(ResultErrorEnum.method_not_support.getMessage());
    }

    default Result<String> clickInvitation(String inviterId, String invitationCode) {
        return Result.failure(ResultErrorEnum.method_not_support.getMessage());
    }

    default Result<UserDTO> wechatRegisterAndLogin(WechatRegisterAndLoginDTO wechatRegisterAndLoginDTO) {
        return Result.failure(ResultErrorEnum.method_not_support.getMessage());
    }

    /**
     * @param boardDTO
     * @return
     */
    default Pair<Result<HandleCellClickResult>, Result<HandleCellClickResult>> changeColumnNumber(BoardDTO boardDTO) {
        return null;
    }

    /**
     * @param userId
     * @param userName
     * @return
     */
    default Result<String> webSocketClose(String userId, String userName) {
        return null;
    }

    /**
     * 获取排行榜数据
     * @return
     */
    default Result<List<RankingDTO>> getRankingList() {
        return null;
    }

    default Result<Boolean> feedBack(String userId, String userName,String content,String subject) {
        return null;
    }

}
