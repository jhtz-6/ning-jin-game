package com.myf.zouding.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  11:52
 * @Description: InvitationCodeDO
 */
@Data
@TableName("invitation_code")
public class InvitationCodeDO extends BaseDO{

    @TableField("invitation_code")
    private String invitationCode;

    @TableField("inviter_id")
    private String inviterId;

    @TableField("invited_id")
    private String invitedId;

    /**
     * 0待处理,1已接受,2已拒绝,3已过期
     */
    @TableField("status")
    private String status;

    @TableField("expired_time")
    private Date expiredTime;

    @TableField("param")
    private String param;

    @TableField("other_fields1")
    private String otherFields1;

    @TableField("other_fields2")
    private String otherFields2;

    @TableField("feature")
    private String feature;


}
