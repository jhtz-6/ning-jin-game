package com.myf.hleper.convert;

import com.myf.hleper.model.dto.RankingDTO;
import com.myf.zouding.database.entity.PlayerStatisticsDO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2024-06-22  14:42
 * @Description: RankConvert
 */
public class RankConvert {

    public static void DO2DTO(PlayerStatisticsDO playerStatisticsDO, RankingDTO rankingDTO) {
        if (Objects.isNull(playerStatisticsDO) || Objects.isNull(rankingDTO)) {
            return;
        }
        if (StringUtils.isNotBlank(playerStatisticsDO.getPlayerId())) {
            rankingDTO.setPlayerId(playerStatisticsDO.getPlayerId());
        }
        if (Objects.nonNull(playerStatisticsDO.getLosses())) {
            rankingDTO.setLosses(playerStatisticsDO.getLosses());
        }
        if (Objects.nonNull(playerStatisticsDO.getTotalGames())) {
            rankingDTO.setTotalGames(playerStatisticsDO.getTotalGames());
        }
        if (Objects.nonNull(playerStatisticsDO.getScore())) {
            rankingDTO.setScore(playerStatisticsDO.getScore());
        }
        if (Objects.nonNull(playerStatisticsDO.getWins())) {
            rankingDTO.setWins(playerStatisticsDO.getWins());
        }
        if (StringUtils.isNotBlank(playerStatisticsDO.getRank())) {
            rankingDTO.setRank(playerStatisticsDO.getRank());
        }
        if (StringUtils.isNotBlank(playerStatisticsDO.getAvatarUrl())) {
            rankingDTO.setAvatarUrl(playerStatisticsDO.getAvatarUrl());
        }
        if (StringUtils.isNotBlank(playerStatisticsDO.getNickName())) {
            rankingDTO.setNickName(playerStatisticsDO.getNickName());
        }
    }


    public static List<RankingDTO> DOList2RankingDTOList(List<PlayerStatisticsDO> playerStatisticsDOS) {
        if (CollectionUtils.isEmpty(playerStatisticsDOS)) {
            return new ArrayList<>();
        }
        return playerStatisticsDOS.stream().map(x -> {
            RankingDTO rankingDTO = new RankingDTO();
            DO2DTO(x, rankingDTO);
            return rankingDTO;
        }).collect(Collectors.toList());
    }
}
