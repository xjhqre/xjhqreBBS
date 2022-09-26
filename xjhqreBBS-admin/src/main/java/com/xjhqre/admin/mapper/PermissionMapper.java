/**
 * Copyright (c) 2022, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.entity.Permission;
import com.xjhqre.admin.entity.dto.PermissionDTO;
import com.xjhqre.admin.entity.vo.PermissionVO;

/**
 * <p>
 * PermissionMapper
 * </p>
 *
 * @author xjhqre
 * @since 9月 17, 2022
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 分页查询权限
     * 
     * @param objectPage
     * @param permissionDTO
     * @return
     */
    IPage<PermissionVO> findPermission(@Param("objectPage") Page<PermissionVO> objectPage,
        @Param("permissionDTO") PermissionDTO permissionDTO);

    /**
     * 查询权限列表
     * 
     * @return
     */
    List<Permission> listAll();
}
