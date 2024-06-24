package com.myf.zouding.database.driver.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myf.zouding.database.driver.ConfigurationRepo;
import com.myf.zouding.database.entity.ConfigurationDO;
import com.myf.zouding.database.mapper.ConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2024-06-23  15:10
 * @Description: ConfigurationRepoImpl
 */
@Service
public class ConfigurationRepoImpl implements ConfigurationRepo {

    @Autowired(required = false)
    private ConfigurationMapper configurationMapper;

    @Override
    public int save(ConfigurationDO configurationDO) {
        return configurationMapper.insert(configurationDO);
    }

    @Override
    public int updateById(ConfigurationDO configurationDO) {
        return configurationMapper.updateById(configurationDO);
    }

    @Override
    public ConfigurationDO selectById(Serializable id) {
        return configurationMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return configurationMapper.deleteById(id);
    }

    @Override
    public List<ConfigurationDO> selectList() {
        return configurationMapper.selectList(new QueryWrapper<>());
    }
}
