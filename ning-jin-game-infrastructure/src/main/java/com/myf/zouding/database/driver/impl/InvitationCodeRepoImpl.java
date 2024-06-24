package com.myf.zouding.database.driver.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myf.zouding.database.driver.InvitationCodeRepo;
import com.myf.zouding.database.entity.InvitationCodeDO;
import com.myf.zouding.database.mapper.InvitationCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  13:37
 * @Description: TODO
 */
@Service
public class InvitationCodeRepoImpl implements InvitationCodeRepo {

    @Autowired(required = false)
    InvitationCodeMapper invitationCodeMapper;

    @Override
    public int save(InvitationCodeDO invitationCodeDO) {
        return invitationCodeMapper.insert(invitationCodeDO);
    }

    @Override
    public int updateById(InvitationCodeDO invitationCodeDO) {
        return invitationCodeMapper.updateById(invitationCodeDO);
    }

    @Override
    public InvitationCodeDO selectById(Serializable id) {
        return invitationCodeMapper.selectById(id);
    }

    @Override
    public int deleteById(Serializable id) {
        return invitationCodeMapper.deleteById(id);
    }

    @Override
    public InvitationCodeDO selectByInvitationCode(String invitationCode) {
        QueryWrapper<InvitationCodeDO> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq(InvitationCodeRepo.INVITATION_CODE, invitationCode);
        return invitationCodeMapper.selectOne(queryWrapper);
    }


}
