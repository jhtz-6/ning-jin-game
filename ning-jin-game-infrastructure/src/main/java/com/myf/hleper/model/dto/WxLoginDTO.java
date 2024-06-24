package com.myf.hleper.model.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-05-01  17:12
 * @Description: WxLoginDTO
 */
@Data
public class WxLoginDTO {
    private String openId;
    private String sessionKey;
    private String unionId;
}
