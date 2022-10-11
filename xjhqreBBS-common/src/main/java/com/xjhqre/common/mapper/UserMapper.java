package com.xjhqre.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.User;

/**
 * 用户表 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface UserMapper {
    /**
     * 根据条件分页查询
     * 
     * @param page
     *            分页
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    IPage<User> findUser(@Param("page") Page<User> page, @Param("user") User user);

    /**
     * 根据条件分页查询已配用户角色列表
     * 
     * @param page
     * @param user
     * @return
     */
    IPage<User> selectAllocatedList(@Param("Page") Page<User> page, @Param("user") User user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUnallocatedList(@Param("user") User user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName
     *            用户名
     * @return 用户对象信息
     */
    User selectUserByUserName(@Param("userName") String userName);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId
     *            用户ID
     * @return 用户对象信息
     */
    User selectUserById(@Param("userId") Long userId);

    /**
     * 新增用户信息
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    int insertUser(@Param("user") User user);

    /**
     * 修改用户信息
     * 
     * @param user
     *            用户信息
     * @return 结果
     */
    int updateUser(User user);

    /**
     * 修改用户头像
     * 
     * @param userName
     *            用户名
     * @param avatar
     *            头像地址
     * @return 结果
     */
    int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     * 
     * @param userName
     *            用户名
     * @param password
     *            密码
     * @return 结果
     */
    int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId
     *            用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param userIds
     *            需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(@Param("userIds") Long[] userIds);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName
     *            用户名称
     * @return 结果
     */
    User checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param mobile
     *            手机号码
     * @return 结果
     */
    User checkPhoneUnique(@Param("mobile") String mobile);

    /**
     * 校验email是否唯一
     *
     * @param email
     *            用户邮箱
     * @return 结果
     */
    User checkEmailUnique(String email);
}