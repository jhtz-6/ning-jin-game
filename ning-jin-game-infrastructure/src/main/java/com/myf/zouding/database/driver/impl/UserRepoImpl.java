package com.myf.zouding.database.driver.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myf.zouding.database.driver.UserRepo;
import com.myf.zouding.database.entity.InvitationCodeDO;
import com.myf.zouding.database.entity.UserDO;
import com.myf.zouding.database.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  13:40
 * @Description: TODO
 */
@Service
public class UserRepoImpl implements UserRepo {

    @Autowired(required = false)
    UserMapper userMapper;

    @Override
    public int save(UserDO userDO) {
        return userMapper.insert(userDO);
    }

    @Override
    public int updateById(UserDO userDO) {
        return userMapper.updateById(userDO);
    }

    @Override
    public UserDO selectById(Serializable id) {
        return userMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return userMapper.deleteById(id);
    }

    @Override
    public UserDO selectByOpenId(String openId) {
        QueryWrapper<UserDO> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq(UserRepo.OPENID,openId);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public UserDO selectByUserId(String userId) {
        QueryWrapper<UserDO> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq(UserRepo.USERID,userId);
        return userMapper.selectOne(queryWrapper);
    }
}
