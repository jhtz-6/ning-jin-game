package com.myf.zouding.database.driver.impl;


import com.myf.zouding.database.driver.RequestLogRepo;
import com.myf.zouding.database.entity.RequestLogDO;
import com.myf.zouding.database.mapper.RequestLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;


/**
 * @Author: myf
 * @CreateTime: 2023-05-19 21:46
 * @Description: RequestLogRepositoryImpl
 */
@Service
public class RequestLogRepoImpl implements RequestLogRepo {

    @Autowired(required = false)
    RequestLogMapper requestLogMapper;


    @Override
    public int save(RequestLogDO requestLogDO) {
        return requestLogMapper.insert(requestLogDO);
    }

    @Override
    public int updateById(RequestLogDO requestLogDO) {
        return requestLogMapper.updateById(requestLogDO);
    }

    @Override
    public RequestLogDO selectById(Serializable id) {
        return requestLogMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return requestLogMapper.deleteById(id);
    }
}
