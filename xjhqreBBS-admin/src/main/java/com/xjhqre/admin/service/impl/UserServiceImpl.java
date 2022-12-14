package com.xjhqre.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.UserMapper;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.admin.service.UserRoleService;
import com.xjhqre.admin.service.UserService;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.Role;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.admin.UserRole;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;

/**
 * 用户 业务层处理
 * 
 * @author xjhqre
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<User> findUser(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(user.getUserId() != null, User::getUserId, user.getUserId())
            .like(user.getUserName() != null, User::getUserName, user.getUserName())
            .eq(user.getStatus() != null, User::getStatus, user.getStatus())
            .like(user.getMobile() != null, User::getMobile, user.getMobile());
        return this.userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 根据条件分页查询已分配角色的用户列表
     * 
     * @param user
     *            用户信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<User> selectAllocatedUserList(User user, Integer pageNum, Integer pageSize) {
        return this.userMapper.selectAllocatedUserList(new Page<>(pageNum, pageSize), user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<User> selectUnallocatedUserList(User user) {
        return this.userMapper.selectUnallocatedUserList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName
     *            用户名
     * @return 用户对象信息
     */
    @Override
    public User selectUserByUserName(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, userName);
        return this.userMapper.selectOne(wrapper);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId
     *            用户ID
     * @return 用户对象信息
     */
    @Override
    public User selectUserById(Long userId) {
        return this.userMapper.selectById(userId);
    }

    /**
     * 查询用户拥有的角色
     * 
     * @param userId
     *            用户名
     * @return 结果
     */
    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<Long> roleIds =
            this.userRoleService.list(wrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
        return this.roleService.listByIds(roleIds);
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public Boolean checkUserNameUnique(User user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = this.userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user
     *            用户信息
     * @return
     */
    @Override
    public Boolean checkPhoneUnique(User user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = this.userMapper.checkPhoneUnique(user.getMobile());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user
     *            用户信息
     * @return
     */
    @Override
    public Boolean checkEmailUnique(User user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = this.userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param userId
     *            用户信息
     */
    @Override
    public void checkUserAllowed(Long userId) {
        // TODO 管理员不能删除管理员
        if (StringUtils.isNotNull(userId) && SecurityUtils.isSuperAdmin(userId)) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public void insertUser(User user) {
        // 新增用户信息
        this.userMapper.insert(user);
        // 新增用户与角色管理
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : user.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        this.userRoleService.saveBatch(list);
    }

    /**
     * 修改保存用户信息
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public int updateUser(User user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        this.userRoleService.remove(wrapper);
        // 新增用户与角色关联
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : user.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        if (!list.isEmpty()) {
            this.userRoleService.saveBatch(list);
        }
        return this.userMapper.updateById(user);
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId
     *            用户ID
     * @return 结果
     */
    @Override
    public void deleteUserById(Long userId) {
        // 校验
        this.checkUserAllowed(userId);
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        this.userRoleService.remove(wrapper);
        // 删除用户
        this.userMapper.deleteById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds
     *            需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            this.checkUserAllowed(userId);
        }
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRole::getUserId, Arrays.asList(userIds));
        this.userRoleService.remove(wrapper);
        // 删除用户
        return this.userMapper.deleteBatchIds(Arrays.asList(userIds));
    }

    /**
     * 注册用户信息
     *
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(User user) {
        return this.userMapper.insert(user) > 0;
    }

}
