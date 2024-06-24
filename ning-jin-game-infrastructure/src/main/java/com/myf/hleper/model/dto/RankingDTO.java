package com.myf.hleper.model.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-22  14:18
 * @Description: RankingDTO
 */
@Data
public class RankingDTO {

    private String playerId;

    private String nickName;

    private String avatarUrl;

    private Integer totalGames;

    private Integer wins;

    private Integer losses;

    private Integer score;

    private String rank;
}
