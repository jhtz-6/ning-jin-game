package com.myf.zouding.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-20  20:00
 * @Description: PlayerStatisticsDO
 */
@Data
@TableName("player_statistics")
public class PlayerStatisticsDO extends BaseDO{


    @TableField("player_id")
    private String playerId;

    @TableField("nick_name")
    private String nickName;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("year_month_str")
    private String yearMonth;

    @TableField("total_games")
    private Integer totalGames;

    @TableField("wins")
    private Integer wins;

    @TableField("losses")
    private Integer losses;

    /**
     * 段位分数
     */
    @TableField("score")
    private Integer score;

    /**
     * 段位
     */
    @TableField("ranks")
    private String rank;

    /**
     * 平局
     */
    @TableField("draws")
    private Integer draws;

    @TableField("other_fields1")
    private String otherFields1;

    @TableField("other_fields2")
    private String otherFields2;
}
