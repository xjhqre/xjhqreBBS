package com.xjhqre.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.entity.AdminRole;
import com.xjhqre.admin.entity.Permission;
import com.xjhqre.admin.entity.RolePermission;
import com.xjhqre.admin.entity.dto.PermissionDTO;
import com.xjhqre.admin.entity.vo.PermissionVO;
import com.xjhqre.admin.mapper.AdminRoleMapper;
import com.xjhqre.admin.mapper.PermissionMapper;
import com.xjhqre.admin.mapper.RolePermissionMapper;
import com.xjhqre.admin.service.AdminService;
import com.xjhqre.admin.service.PermissionService;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.common.exception.ErrorEnume;
import com.xjhqre.common.exception.MyServiceException;

/**
 * Created by lhr on 17-8-1.
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 查询用户所有权限
     * 
     * @param uid
     * @return
     */
    @Override
    public List<Permission> listUserPermission(Long uid) {
        // 查询出当前用户对应的所有角色
        LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminRole::getAdminUid, uid);
        List<AdminRole> adminRoles = this.adminRoleMapper.selectList(queryWrapper);
        if (adminRoles == null || adminRoles.size() == 0) {
            throw new MyServiceException(ErrorEnume.ROLE_DOES_NOT_EXIST);
        }
        List<Long> roleIds = adminRoles.stream().map(AdminRole::getRoleUid).collect(Collectors.toList());

        // 查询出所有角色对应的所有权限，权限可能会有重复，进行去重
        List<RolePermission> rolePermissions = this.rolePermissionMapper.selectBatchIds(roleIds);
        // 取出所有权限id并去重
        List<Long> permissionUidList =
            rolePermissions.stream().map(RolePermission::getPermissionUid).distinct().collect(Collectors.toList());
        // 查询出所有权限实体
        return this.permissionMapper.selectBatchIds(permissionUidList);
    }

    /**
     * 加载类型为type的权限
     *
     * @param uid
     *            当前登陆管理员的uid
     * @param type
     * @return
     */
    @Override
    public List<Permission> loadUserPermissionByType(Long uid, Integer type) {
        List<Permission> permissions = this.listUserPermission(uid);
        // 返回类型为type的权限
        return permissions.stream().filter(permission -> Objects.equals(permission.getType(), type))
            .collect(Collectors.toList());
    }

    /**
     * 分页查询权限
     * 
     * @param permissionDTO
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<PermissionVO> findPermission(PermissionDTO permissionDTO, int pageNum, int pageSize) {
        return this.permissionMapper.findPermission(new Page<>(pageNum, pageSize), permissionDTO);
    }

    /**
     * 批量删除权限
     * 
     * @param ids
     */
    @Override
    public void deleteByIds(Permission[] ids) {
        this.permissionMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 查询权限列表
     * 
     * @return
     */
    @Override
    public List<Permission> listAll() {
        return this.permissionMapper.listAll();
    }

}
