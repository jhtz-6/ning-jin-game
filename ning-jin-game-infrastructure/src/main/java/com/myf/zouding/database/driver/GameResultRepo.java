package com.myf.zouding.database.driver;

import com.myf.common.enmus.GameTypeEnum;
import com.myf.zouding.database.entity.GameResultDO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * @author myf
 */
public interface GameResultRepo  extends BaseRepo<GameResultDO>{

    List<GameResultDO> selectWinCounts(List<String> playerIds, GameTypeEnum gameType);
    List<GameResultDO> selectLoseCounts(List<String> playerIds, GameTypeEnum gameType);

}
