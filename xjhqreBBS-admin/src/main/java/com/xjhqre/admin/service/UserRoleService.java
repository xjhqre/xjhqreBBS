package com.xjhqre.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.admin.UserRole;

/**
 * 角色业务层
 * 
 * @author xjhqre
 */
public interface UserRoleService extends IService<UserRole> {
    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId
     *            角色ID
     * @return 结果
     */
    int countUserRoleByRoleId(Long roleId);
}
