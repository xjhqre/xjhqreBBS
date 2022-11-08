package com.xjhqre.common.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.Role;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.admin.UserRole;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.sms.Message3;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.mapper.RoleMapper;
import com.xjhqre.common.mapper.UserMapper;
import com.xjhqre.common.mapper.UserRoleMapper;
import com.xjhqre.common.service.Message3Service;
import com.xjhqre.common.service.UserService;
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
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private Message3Service message3Service;

    // @Autowired
    // private ISysConfigService configService;

    @Autowired
    protected Validator validator;

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
        return this.userMapper.findUser(new Page<>(pageNum, pageSize), user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<User> selectAllocatedList(User user, Integer pageNum, Integer pageSize) {
        return this.userMapper.selectAllocatedList(new Page<>(pageNum, pageSize), user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<User> selectUnallocatedList(User user) {
        return this.userMapper.selectUnallocatedList(user);
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
        return this.userMapper.selectUserByUserName(userName);
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
        return this.userMapper.selectUserById(userId);
    }

    /**
     * 查询用户拥有的角色
     * 
     * @param userName
     *            用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<Role> list = this.roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(Role::getRoleName).collect(Collectors.joining(","));
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
    public int insertUser(User user) {
        // 新增用户信息
        int rows = this.userMapper.insertUser(user);
        // 新增用户与角色管理
        this.insertUserRole(user);
        return rows;
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
        return this.userMapper.insertUser(user) > 0;
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
        this.userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色关联
        this.insertUserRole(user);
        return this.userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId
     *            用户ID
     * @param roleIds
     *            角色组
     */
    @Override
    public void insertUserAuth(Long userId, Long[] roleIds) {
        // 先删除用户角色信息
        this.userRoleMapper.deleteUserRoleByUserId(userId);
        this.insertUserRole(userId, Arrays.asList(roleIds));
    }

    /**
     * 修改用户状态
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(User user) {
        return this.userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(User user) {
        return this.userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userName
     *            用户名
     * @param avatar
     *            头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return this.userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(User user) {
        return this.userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName
     *            用户名
     * @param password
     *            密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return this.userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user
     *            用户对象
     */
    public void insertUserRole(User user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     * 
     * @param userId
     *            用户ID
     * @param roleIds
     *            角色组
     */
    public void insertUserRole(Long userId, List<Long> roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<UserRole> list = new ArrayList<>(roleIds.size());
            for (Long roleId : roleIds) {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            this.userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId
     *            用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        this.userRoleMapper.deleteUserRoleByUserId(userId);
        return this.userMapper.deleteUserById(userId);
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
        this.userRoleMapper.deleteUserRole(userIds);
        // 删除用户
        return this.userMapper.deleteUserByIds(userIds);
    }

    /**
     * 查找指定用户发布的文章
     * 
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findUserArticle(Article article, Integer pageNum, Integer pageSize) {
        return this.userMapper.findUserArticle(new Page<Article>(pageNum, pageSize), article);
    }

    /**
     * 分页查询用户信息-点赞收藏
     * 
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Message3> findMessage3(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Message3> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message3::getCollectedId, userId).eq(Message3::getDelFlag, "0");
        Page<Message3> page = this.message3Service.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Message3> records = page.getRecords();
        for (Message3 message3 : records) {
            message3.setStatus("1"); // 设置已读
        }
        this.message3Service.updateBatchById(records);
        return page;
    }

}
