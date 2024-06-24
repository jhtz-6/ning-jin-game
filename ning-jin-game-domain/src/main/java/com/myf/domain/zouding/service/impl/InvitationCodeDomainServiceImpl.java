package com.myf.domain.zouding.service.impl;

import com.myf.common.constant.InvitationCodeConstant;
import com.myf.common.util.InvitationCodeUtils;
import com.myf.common.util.MD5Utils;
import com.myf.domain.zouding.service.InvitationCodeDomainService;
import com.myf.hleper.model.res.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  10:42
 * @Description: TODO
 */
@Slf4j
@Service
public class InvitationCodeDomainServiceImpl implements InvitationCodeDomainService {


    @Override
    public Result<String> generateInvitationCode() {
        //根据userid和时间戳来生成邀请码
        String invitationCode = null;
        try {
            invitationCode =System.currentTimeMillis()+ InvitationCodeUtils.getRandomLetters(false,2);
        } catch (Exception e) {
            log.error("generateInvitationCode.error",e);
        }
        if(Objects.isNull(invitationCode)){
            return Result.failure("查询失败,请稍后重试");
        }
        return Result.success(invitationCode);
    }

    @Override
    public Result<String> getUserIdByInvitationCode(String invitationCode) {
        if(StringUtils.isBlank(invitationCode)){
            return Result.failure("invitationCode cannot be null");
        }
        try {
            String decrypt = MD5Utils.decrypt(invitationCode, InvitationCodeConstant.MD5_KEY);
            String userId = decrypt.split(InvitationCodeConstant.MD5_TEXT_SEPARATOR)[0];
            if(StringUtils.isNotBlank(userId)){
                return Result.success(userId);
            }
        } catch (Exception e) {
            log.error("generateInvitationCode.error.invitationCode:{}",invitationCode,e);
        }
        return Result.failure("邀请码失败,请稍后重试");
    }


}
