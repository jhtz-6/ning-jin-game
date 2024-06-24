package com.myf.hleper.model.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-23  15:27
 * @Description: TencentMailDTO
 */
@Data
public class TencentMailDTO {

   private String subject;

    private String text;

    private String targetMailAddress;
}
