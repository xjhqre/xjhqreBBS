package com.xjhqre.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.sms.Message3;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.portal.mapper.UserMapper;
import com.xjhqre.portal.service.Message3Service;
import com.xjhqre.portal.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户 业务层处理
 * 
 * @author xjhqre
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Message3Service message3Service;

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
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        return this.userMapper.selectOne(wrapper);
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

    /**
     * 修改用户密码
     * 
     * @param userName
     * @param encryptPassword
     * @return
     */
    @Override
    public void resetUserPwd(Long userId, String encryptPassword) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, userId).set(User::getPassword, encryptPassword);
        this.userMapper.update(null, wrapper);
    }

    /**
     * 上传头像
     * 
     * @param userId
     * @param avatarUrl
     */
    @Override
    public void updateUserAvatar(Long userId, String avatarUrl) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, userId).set(User::getAvatar, avatarUrl);
        this.userMapper.update(null, wrapper);
    }

}
