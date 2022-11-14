package com.xjhqre.portal.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.admin.Role;

/**
 * 角色业务层
 * 
 * @author xjhqre
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据条件分页查询角色数据
     * 
     * @param role
     *            角色信息
     * @return 角色数据集合信息
     */
    List<Role> selectRoleList(Role role);

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Role> findRole(Role role, Integer pageNum, Integer pageSize);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId
     *            用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(Long userId);

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId
     *            角色ID
     * @return 角色对象信息
     */
    Role selectRoleById(Long roleId);
}
