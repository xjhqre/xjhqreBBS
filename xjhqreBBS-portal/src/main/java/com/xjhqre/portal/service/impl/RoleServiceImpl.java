package com.xjhqre.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.admin.Role;
import com.xjhqre.portal.mapper.RoleMapper;
import com.xjhqre.portal.mapper.UserRoleMapper;
import com.xjhqre.portal.service.RoleService;

/**
 * 角色 业务层处理
 * 
 * @author xjhqre
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 根据条件查询角色列表
     * 
     * @param role
     *            角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<Role> selectRoleList(Role role) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(role.getRoleId() != null, Role::getRoleId, role.getRoleId())
            .like(role.getRoleName() != null, Role::getRoleName, role.getRoleName())
            .eq(role.getStatus() != null, Role::getStatus, role.getStatus())
            .like(role.getRoleKey() != null, Role::getRoleKey, role.getRoleKey()).orderByDesc(Role::getRoleSort);
        return this.roleMapper.selectList(wrapper);
    }

    @Override
    public IPage<Role> findRole(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(role.getRoleId() != null, Role::getRoleId, role.getRoleId())
            .like(role.getRoleName() != null, Role::getRoleName, role.getRoleName())
            .eq(role.getStatus() != null, Role::getStatus, role.getStatus())
            .like(role.getRoleKey() != null, Role::getRoleKey, role.getRoleKey()).orderByDesc(Role::getStatus);
        return this.roleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 根据用户ID查询角色
     * 
     * @param userId
     *            用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return this.roleMapper.selectRolesByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId
     *            角色ID
     * @return 角色对象信息
     */
    @Override
    public Role selectRoleById(Long roleId) {
        return this.roleMapper.selectRoleById(roleId);
    }

}
