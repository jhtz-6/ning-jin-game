package com.myf.common.model;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-12-16  15:49
 * @Description: RequestLogDTO
 */
@Data
public class RequestLogDTO {

    private String userName;

    private String requestUrl;

    private String requestMethod;

    private String requestParams;

    private String clientType;

    private String ipAddress;

    private String location;

    private String userAgent;

}
