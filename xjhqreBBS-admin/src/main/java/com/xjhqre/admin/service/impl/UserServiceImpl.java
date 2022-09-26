package com.xjhqre.admin.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.entity.User;
import com.xjhqre.admin.entity.dto.UserDTO;
import com.xjhqre.admin.entity.vo.UserVO;
import com.xjhqre.admin.mapper.UserMapper;
import com.xjhqre.admin.service.UserService;

/**
 * @Author LHR Create By 2017/8/25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询用户信息
     * 
     * @param userDTO
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<UserVO> findUser(UserDTO userDTO, Integer pageNum, Integer pageSize) {
        return this.userMapper.findUser(new Page<>(pageNum, pageSize), userDTO);
    }

    @Override
    public Boolean hasSameEmail(String email) {
        Integer res = this.userMapper.hasSameEmail(email);
        return res == 1;
    }

    @Override
    public Boolean hasSameUsername(String userName) {
        Integer res = this.userMapper.hasSameUsername(userName);
        return res == 1;
    }

    /**
     * 添加用户
     * 
     * @param user
     */
    @Override
    public void saveUser(User user) {
        this.userMapper.insert(user);
    }

    /**
     * 批量删除用户
     * 
     * @param ids
     */
    @Override
    public void deleteUserByIds(Long[] ids) {
        this.userMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 添加用户角色
     * 
     * @param userUid
     * @param roleIds
     */
    @Override
    public void saveUserRoles(Long userUid, Long[] roleIds) {
        this.userMapper.saveUserRoles(userUid, roleIds);
    }

    /**
     * 启用用户
     * 
     * @param ids
     */
    @Override
    public void enableUser(Long[] ids) {
        this.userMapper.enableUser(ids);
    }

    /**
     * 批量禁用用户
     * 
     * @param ids
     */
    @Override
    public void disableUser(Long[] ids) {
        this.userMapper.disableUser(ids);
    }

}
