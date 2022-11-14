package com.xjhqre.portal.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.domain.admin.Role;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.portal.service.MenuService;

/**
 * 用户权限处理
 * 
 * @author xjhqre
 */
@Component
public class PermissionService {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单数据权限
     * 
     * @param user
     *            用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(User user) {
        Set<String> perms = new HashSet<>();
        // 超级管理员拥有所有权限
        if (user.isSuperAdmin()) {
            perms.add("*:*:*");
        } else {
            List<Role> roles = user.getRoles();
            if (roles != null && !roles.isEmpty() && roles.size() > 1) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (Role role : roles) {
                    // 使用Set对权限进行去重
                    Set<String> rolePerms = this.menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(this.menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
