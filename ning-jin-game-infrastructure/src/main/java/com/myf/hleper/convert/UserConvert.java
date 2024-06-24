package com.myf.hleper.convert;

import com.alibaba.fastjson.JSON;
import com.myf.hleper.model.dto.UserDTO;
import com.myf.zouding.database.entity.InvitationCodeDO;
import com.myf.zouding.database.entity.UserDO;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  19:22
 * @Description: UserConvert
 */
public class UserConvert {

    public static UserDTO DO2DTO(UserDO userDO, InvitationCodeDO invitationCodeDO){
        UserDTO userDTO =new UserDTO();
        if(Objects.isNull(userDO)){
            return userDTO;
        }
        if(StringUtils.isNotBlank(userDO.getNickName())){
            userDTO.setUserName(userDO.getNickName());
        }
        if(StringUtils.isNotBlank(userDO.getOpenId())){
            userDTO.setUserId(userDO.getOpenId());
        }
        if(StringUtils.isNotBlank(userDO.getAvatarUrl())){
            userDTO.setAvatarUrl(userDO.getAvatarUrl());
        }
        if(StringUtils.isNotBlank(invitationCodeDO.getParam())){
            Map map = JSON.parseObject(invitationCodeDO.getParam(), Map.class);
            userDTO.setColumnNumber(Integer.valueOf((String)map.get("level")));
        }
        return userDTO;
    }
}
