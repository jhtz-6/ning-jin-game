package com.myf.zouding.database.driver;

import com.myf.zouding.database.entity.PlayerStatisticsDO;
import com.myf.zouding.database.param.PlayerStatisticsQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface PlayerStatisticsRepo extends BaseRepo<PlayerStatisticsDO>{

    List<PlayerStatisticsDO> selectRankingListByParam(PlayerStatisticsQueryParam queryParam);

    Integer incrementWinsByPlayerId(String playerId);

    PlayerStatisticsDO selectByPlayerId(String playerId);

    Integer incrementLossesByPlayerId(String playerId);

}
