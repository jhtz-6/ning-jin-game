package com.myf.zouding.database.driver.impl;

import com.myf.zouding.database.driver.PlayerStatisticsRepo;
import com.myf.zouding.database.entity.PlayerStatisticsDO;
import com.myf.zouding.database.mapper.PlayerStatisticsMapper;
import com.myf.zouding.database.param.PlayerStatisticsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2024-06-20  20:14
 * @Description: PlayerStatisticsRepoImpl
 */
@Service
public class PlayerStatisticsRepoImpl implements PlayerStatisticsRepo {

    @Autowired(required = false)
    PlayerStatisticsMapper playerStatisticsMapper;


    @Override
    public int save(PlayerStatisticsDO playerStatisticsDO) {
        return playerStatisticsMapper.insert(playerStatisticsDO);
    }

    @Override
    public int updateById(PlayerStatisticsDO playerStatisticsDO) {
        return playerStatisticsMapper.updateById(playerStatisticsDO);
    }

    @Override
    public PlayerStatisticsDO selectById(Serializable id) {
        return playerStatisticsMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return playerStatisticsMapper.deleteById(id);
    }

    @Override
    public List<PlayerStatisticsDO> selectRankingListByParam(PlayerStatisticsQueryParam queryParam) {
        return playerStatisticsMapper.selectRankingListByParam(queryParam);
    }

    @Override
    public Integer incrementWinsByPlayerId(String playerId) {
        return playerStatisticsMapper.incrementWinsByPlayerId(playerId);
    }

    @Override
    public PlayerStatisticsDO selectByPlayerId(String playerId) {
        return playerStatisticsMapper.selectByPlayerId(playerId);
    }

    @Override
    public Integer incrementLossesByPlayerId(String playerId) {
        return playerStatisticsMapper.incrementLossesByPlayerId(playerId);
    }
}
