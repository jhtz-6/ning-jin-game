package com.myf.domain.zouding.service;

import com.myf.hleper.model.res.Result;

/**
 * @author myf
 */
public interface InvitationCodeDomainService {

    /**
     * 根据userId来获取邀请码
     * @param userId
     * @return
     */
    Result<String> generateInvitationCode();

    /**
     * 根据邀请码来获取userId
     * @param invitationCode
     * @return
     */
    Result<String> getUserIdByInvitationCode(String invitationCode);
}
