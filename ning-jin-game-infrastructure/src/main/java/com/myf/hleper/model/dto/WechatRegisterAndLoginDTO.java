package com.myf.hleper.model.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  15:57
 * @Description: WechatRegisterDTO
 */
@Data
public class WechatRegisterAndLoginDTO {


    private String jsCode;

    private String openId;

    private String nickName;

    private String avatarUrl;

    private Integer gender;
}
