package com.xjhqre.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.entity.Role;
import com.xjhqre.admin.entity.dto.RoleDTO;
import com.xjhqre.admin.entity.vo.RoleVO;
import com.xjhqre.admin.mapper.RoleMapper;
import com.xjhqre.admin.service.RoleService;

/**
 * Created by lhr on 17-8-1.
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleVO> listRolesByUserId(Long uid) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getUid, uid);
        List<Role> roles = this.roleMapper.selectList(queryWrapper);
        // 将 role 包装成 roleVO 返回
        return roles.stream().map(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        }).collect(Collectors.toList());
    }

    /**
     * 分页查询角色信息
     * 
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<RoleVO> findRole(RoleDTO roleDTO, int pageNum, int pageSize) {
        return this.roleMapper.findRole(new Page<>(pageNum, pageSize), roleDTO);
    }

    /**
     * 添加角色信息
     *
     * @param role
     * @param permissionIds
     */
    @Override
    public void saveRole(Role role, Long[] permissionIds) {
        // 添加角色
        this.roleMapper.insert(role);

        // 添加角色权限
        this.roleMapper.saveBatchRolePermission(role.getUid(), permissionIds);
    }

    /**
     * 批量删除角色
     * 
     * @param ids
     */
    @Override
    public void deleteRole(Long[] ids) {
        this.roleMapper.deleteBatchIds(Arrays.asList(ids));
    }

}
