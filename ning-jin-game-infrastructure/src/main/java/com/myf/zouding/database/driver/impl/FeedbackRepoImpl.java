package com.myf.zouding.database.driver.impl;

import com.myf.zouding.database.driver.FeedbackRepo;
import com.myf.zouding.database.entity.FeedbackDO;
import com.myf.zouding.database.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author: myf
 * @CreateTime: 2024-06-23  16:00
 * @Description: FeedbackRepoImpl
 */
@Service
public class FeedbackRepoImpl implements FeedbackRepo {

    @Autowired(required = false)
    private FeedbackMapper feedbackMapper;

    @Override
    public int save(FeedbackDO feedbackDO) {
        return feedbackMapper.insert(feedbackDO);
    }

    @Override
    public int updateById(FeedbackDO feedbackDO) {
        return feedbackMapper.updateById(feedbackDO);
    }

    @Override
    public FeedbackDO selectById(Serializable id) {
        return feedbackMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return feedbackMapper.deleteById(id);
    }
}
