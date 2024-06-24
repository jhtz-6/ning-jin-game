package com.myf.zouding.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myf.zouding.database.entity.GameResultDO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * @author myf
 */
public interface GameResultMapper extends BaseMapper<GameResultDO> {

    /**
     *
     * @param playerIds
     * @param gameType
     * @return
     */
    List<GameResultDO> selectWinCountsByGameType(@Param("playerIds") List<String> playerIds,@Param("gameType") String gameType);

    /**
     *
     * @param playerIds
     * @param gameType
     * @return
     */
    List<GameResultDO> selectLoseCountsByGameType(@Param("playerIds") List<String> playerIds,@Param("gameType") String gameType);

}
