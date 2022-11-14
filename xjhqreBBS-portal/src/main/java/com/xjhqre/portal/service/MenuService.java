package com.xjhqre.portal.service;

import java.util.List;
import java.util.Set;

import com.xjhqre.common.domain.admin.Menu;

/**
 * 菜单 业务层
 * 
 * @author xjhqre
 */
public interface MenuService {

    /**
     * 根据用户查询系统菜单列表
     * 
     * @param menu
     *            菜单信息
     * @param userId
     *            用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuList(Menu menu, Long userId);

    /**
     * 根据用户ID查询权限
     * 
     * @param userId
     *            用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId
     *            角色ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId
     *            角色ID
     * @return 权限列表
     */
    List<Menu> selectMenuListByRoleId(Long roleId);

    /**
     * 构建前端所需要树结构
     * 
     * @param menus
     *            菜单列表
     * @return 树结构列表
     */
    List<Menu> buildMenuTree(List<Menu> menus);

    /**
     * 根据菜单ID查询信息
     * 
     * @param menuId
     *            菜单ID
     * @return 菜单信息
     */
    Menu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     * 
     * @param menuId
     *            菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);
}
