package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.entity.Role;
import com.xjhqre.admin.entity.dto.RoleDTO;
import com.xjhqre.admin.entity.vo.RoleVO;

/**
 * <p>
 * RoleMapper
 * </p>
 *
 * @author xjhqre
 * @since 9月 17, 2022
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    IPage<RoleVO> findRole(@Param("objectPage") Page<RoleVO> objectPage, @Param("roleDTO") RoleDTO roleDTO);

    void saveBatchRolePermission(@Param("uid") Long uid, @Param("permissionIds") Long[] permissionIds);
}
