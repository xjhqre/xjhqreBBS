package com.xjhqre.portal.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.sms.Message3;

/**
 * 用户 业务层
 * 
 * @author xjhqre
 */
public interface UserService extends IService<User> {

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<User> findUser(User user, Integer pageNum, Integer pageSize);

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    IPage<User> selectAllocatedList(User user, Integer pageNum, Integer pageSize);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUnallocatedList(User user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName
     *            用户名
     * @return 用户对象信息
     */
    User selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId
     *            用户ID
     * @return 用户对象信息
     */
    User selectUserById(Long userId);

    /**
     * 校验用户名称是否唯一
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    Boolean checkUserNameUnique(User user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user
     *            用户信息
     * @return 结果
     */
    Boolean checkPhoneUnique(User user);

    /**
     * 校验email是否唯一
     *
     * @param user
     *            用户信息
     * @return 结果
     */
    Boolean checkEmailUnique(User user);

    /**
     * 注册用户信息
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    boolean registerUser(User user);

    /**
     * 分页查询用户信息-点赞收藏
     * 
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Message3> findMessage3(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 修改用户密码
     * 
     * @param userId
     * @param encryptPassword
     * @return
     */
    void resetUserPwd(Long userId, String encryptPassword);

    void updateUserAvatar(Long userId, String avatarUrl);
}
