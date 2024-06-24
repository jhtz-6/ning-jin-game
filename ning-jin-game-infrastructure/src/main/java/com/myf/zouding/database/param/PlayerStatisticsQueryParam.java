package com.myf.zouding.database.param;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2024-06-22  14:30
 * @Description: PlayerStatisticsQueryParam
 */
@Data
public class PlayerStatisticsQueryParam extends BaseQueryParam{

    private String playerId;

    private String yearMonth;

    private Integer playerSize = 20;

}
