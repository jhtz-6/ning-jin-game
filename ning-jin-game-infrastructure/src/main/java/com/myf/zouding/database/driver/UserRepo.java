package com.myf.zouding.database.driver;


import com.myf.zouding.database.entity.InvitationCodeDO;
import com.myf.zouding.database.entity.UserDO;

/**
 * @author myf
 */
public interface UserRepo extends BaseRepo<UserDO>{

    UserDO selectByOpenId(String openId);

    UserDO selectByUserId(String userId);


    String OPENID = "open_id";

    String USERID = "user_id";

}
