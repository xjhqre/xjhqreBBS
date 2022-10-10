package com.xjhqre.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.RoleMapper;
import com.xjhqre.admin.mapper.RoleMenuMapper;
import com.xjhqre.admin.mapper.UserRoleMapper;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.entity.Role;
import com.xjhqre.common.domain.entity.RoleMenu;
import com.xjhqre.common.domain.entity.UserRole;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;

/**
 * 角色 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

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
        return this.roleMapper.selectRoleList(role);
    }

    @Override
    public IPage<Role> findRole(Role role, Integer pageNum, Integer pageSize) {
        return this.roleMapper.findRole(new Page<>(pageNum, pageSize), role);
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
     * 根据用户ID查询权限
     * 
     * @param userId
     *            用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<Role> perms = this.roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (Role perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<Role> selectRoleAll() {
        return this.selectRoleList(new Role());
    }

    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId
     *            用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return this.roleMapper.selectRoleListByUserId(userId);
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

    /**
     * 校验角色名称是否唯一
     * 
     * @param role
     *            角色信息
     * @return 结果
     */
    @Override
    public Boolean checkRoleNameUnique(Role role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Role info = this.roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role
     *            角色信息
     * @return 结果
     */
    @Override
    public Boolean checkRoleKeyUnique(Role role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Role info = this.roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param roleId
     *            角色信息
     */
    @Override
    public void checkRoleAllowed(Long roleId) {
        if (StringUtils.isNotNull(roleId) && SecurityUtils.isSuperAdmin(roleId)) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId
     *            角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return this.userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     * 
     * @param role
     *            角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(Role role) {
        // 新增角色信息
        this.roleMapper.insertRole(role);
        return this.insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     * 
     * @param role
     *            角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(Role role) {
        // 修改角色信息
        this.roleMapper.updateRole(role);
        // 删除角色与菜单关联
        this.roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return this.insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     * 
     * @param role
     *            角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(Role role) {
        return this.roleMapper.updateRole(role);
    }

    /**
     * 新增角色菜单信息
     * 
     * @param role
     *            角色对象
     */
    public int insertRoleMenu(Role role) {
        int rows = 1;
        // 新增用户与角色管理
        List<RoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = this.roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId
     *            角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        this.roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return this.roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds
     *            需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            this.checkRoleAllowed(roleId);
            Role role = this.selectRoleById(roleId);
            if (this.countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        this.roleMenuMapper.deleteRoleMenu(roleIds);
        return this.roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 取消授权用户角色
     * 
     * @param userRole
     *            用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(UserRole userRole) {
        return this.userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId
     *            角色ID
     * @param userIds
     *            需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return this.userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId
     *            角色ID
     * @param userIds
     *            需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<UserRole> list = new ArrayList<>();
        for (Long userId : userIds) {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return this.userRoleMapper.batchUserRole(list);
    }
}
