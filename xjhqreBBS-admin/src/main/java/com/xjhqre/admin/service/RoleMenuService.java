package com.xjhqre.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.admin.RoleMenu;

/**
 * 角色业务层
 * 
 * @author xjhqre
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long roleId);
}
