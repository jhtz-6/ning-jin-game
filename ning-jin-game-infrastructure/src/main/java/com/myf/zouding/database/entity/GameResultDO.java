package com.myf.zouding.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-05-23  21:38
 * @Description: TODO
 */
@Data
@TableName("game_result")
public class GameResultDO extends BaseDO{

    /**
     * 单机、匹配、邀请
     */
    @TableField("game_type")
    private String gameType;

    @TableField("player1_id")
    private String player1Id;

    @TableField("player1_name")
    private String player1Name;

    @TableField("player1_score")
    private String player1Score;

    @TableField("player2_id")
    private String player2Id;

    @TableField("player2_name")
    private String player2Name;

    @TableField("player2_score")
    private String player2Score;

    @TableField("winner_id")
    private String winnerId;

    @TableField("feature")
    private String feature;

    @TableField("other_fields1")
    private String otherFields1;

    private String count;
}
