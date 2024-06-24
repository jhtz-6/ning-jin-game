package com.myf.zouding.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  11:47
 * @Description: UserDO
 */
@Data
@TableName("user")
public class UserDO extends BaseDO{

    @TableField("user_id")
    private String userId;

    @TableField("open_id")
    private String openId;

    @TableField("session_key")
    private String sessionKey;

    @TableField("nick_name")
    private String nickName;

    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 用户的性别 0未知 1男 2女
     */
    @TableField("gender")
    private Integer gender;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField("last_login_time")
    private Date lastLoginTime;

    /**
     * 0无效,1有效,2活跃,3不活跃
     */
    @TableField("status")
    private String status;

    @TableField("score")
    private Integer score;

    @TableField("other_fields1")
    private String otherFields1;

    @TableField("other_fields2")
    private String otherFields2;

    @TableField("other_fields3")
    private String otherFields3;

    @TableField("feature")
    private String feature;
}
