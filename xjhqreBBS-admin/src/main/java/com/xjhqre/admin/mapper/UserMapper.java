/**
 * Copyright (c) 2022, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.entity.User;
import com.xjhqre.admin.entity.dto.UserDTO;
import com.xjhqre.admin.entity.vo.UserVO;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author xjhqre
 * @since 9月 21, 2022
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 分页查询用户
     * 
     * @param objectPage
     * @param userDTO
     * @return
     */
    IPage<UserVO> findUser(@Param("objectPage") Page<Object> objectPage, @Param("userDTO") UserDTO userDTO);

    /**
     * 查询邮箱是否重复
     * 
     * @param email
     * @return
     */
    Integer hasSameEmail(@Param("email") String email);

    /**
     * 是否已存在相同用户名
     * 
     * @param userName
     * @return
     */
    Integer hasSameUsername(@Param("userName") String userName);

    /**
     * 设置用户角色
     * 
     * @param userUid
     * @param roleIds
     */
    void saveUserRoles(@Param("userUid") Long userUid, @Param("roleIds") Long[] roleIds);

    /**
     * 启用用户
     * 
     * @param ids
     */
    void enableUser(@Param("ids") Long[] ids);

    /**
     * 禁用用户
     * 
     * @param ids
     */
    void disableUser(@Param("ids") Long[] ids);
}
