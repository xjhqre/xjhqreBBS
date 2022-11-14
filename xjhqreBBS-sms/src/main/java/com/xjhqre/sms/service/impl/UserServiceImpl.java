package com.xjhqre.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.sms.mapper.UserMapper;
import com.xjhqre.sms.service.UserService;

/**
 * 用户 业务层处理
 * 
 * @author xjhqre
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User selectUserById(Long userId) {
        return this.userMapper.selectById(userId);
    }
}
