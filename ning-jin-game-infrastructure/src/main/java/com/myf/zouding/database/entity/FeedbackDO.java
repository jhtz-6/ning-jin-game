package com.myf.zouding.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-23  15:57
 * @Description: FeedbackDO
 */
@Data
@TableName("feedback")
public class FeedbackDO extends BaseDO{

    @TableField("open_id")
    private String openId;

    @TableField("user_name")
    private String userName;

    @TableField("content")
    private String content;

    @TableField("subject")
    private String subject;

    @TableField("effective")
    private String effective;

    @TableField("is_reply")
    private String isReply;

    @TableField("reply_content")
    private String replyContent;
}
