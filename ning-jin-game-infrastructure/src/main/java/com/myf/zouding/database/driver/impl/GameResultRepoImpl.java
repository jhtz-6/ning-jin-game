package com.myf.zouding.database.driver.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myf.common.enmus.GameTypeEnum;
import com.myf.zouding.database.driver.GameResultRepo;
import com.myf.zouding.database.driver.InvitationCodeRepo;
import com.myf.zouding.database.entity.GameResultDO;
import com.myf.zouding.database.mapper.GameResultMapper;
import com.myf.zouding.database.mapper.InvitationCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: myf
 * @CreateTime: 2024-05-23  21:42
 * @Description: GameResultRepoImpl
 */
@Service
public class GameResultRepoImpl implements GameResultRepo {

    @Autowired(required = false)
    GameResultMapper gameResultMapper;


    @Override
    public int save(GameResultDO gameResultDO) {
        return gameResultMapper.insert(gameResultDO);
    }

    @Override
    public int updateById(GameResultDO gameResultDO) {
        return gameResultMapper.updateById(gameResultDO);
    }

    @Override
    public GameResultDO selectById(Serializable id) {
        return gameResultMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return gameResultMapper.deleteById(id);
    }

    @Override
    public List<GameResultDO> selectWinCounts(List<String> playerIds, GameTypeEnum gameType) {
        return gameResultMapper.selectWinCountsByGameType(playerIds,gameType.name());
    }

    @Override
    public List<GameResultDO> selectLoseCounts(List<String> playerIds, GameTypeEnum gameType) {
        return gameResultMapper.selectLoseCountsByGameType(playerIds,gameType.name());
    }
}
