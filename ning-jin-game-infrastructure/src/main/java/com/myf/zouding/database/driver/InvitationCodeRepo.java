package com.myf.zouding.database.driver;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.myf.zouding.database.entity.InvitationCodeDO;

import java.io.Serializable;

/**
 * @author myf
 */
public interface InvitationCodeRepo extends BaseRepo<InvitationCodeDO>{

    InvitationCodeDO selectByInvitationCode(String invitationCode);



    String INVITATION_CODE = "invitation_code";
}
