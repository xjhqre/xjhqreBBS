package com.xjhqre.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.User;

/**
 * 用户表 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据条件分页查询已配用户角色列表
     * 
     * @param page
     * @param user
     * @return
     */
    IPage<User> selectAllocatedUserList(@Param("Page") Page<User> page, @Param("user") User user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user
     *            用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUnallocatedUserList(@Param("user") User user);

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
