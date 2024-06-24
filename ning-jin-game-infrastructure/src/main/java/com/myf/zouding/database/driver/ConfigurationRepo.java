package com.myf.zouding.database.driver;

import com.myf.zouding.database.entity.ConfigurationDO;

import java.util.List;

/**
 * @author myf
 */
public interface ConfigurationRepo extends BaseRepo<ConfigurationDO>{

    List<ConfigurationDO> selectList();
}
