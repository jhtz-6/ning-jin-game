package com.myf.zouding.database.param;

import lombok.Data;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2024-06-22  14:30
 * @Description: BaseQueryParam
 */
@Data
public class BaseQueryParam {

    private String id;

    private Date createTime;

    private Date updateTime;
}
