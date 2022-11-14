package com.xjhqre.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.RoleMenuMapper;
import com.xjhqre.admin.service.RoleMenuService;
import com.xjhqre.common.domain.admin.RoleMenu;

/**
 * 角色 业务层处理
 * 
 * @author xjhqre
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Override
    public void deleteRoleMenuByRoleId(Long roleId) {
        this.roleMenuMapper.deleteRoleMenuByRoleId(roleId);
    }
}
