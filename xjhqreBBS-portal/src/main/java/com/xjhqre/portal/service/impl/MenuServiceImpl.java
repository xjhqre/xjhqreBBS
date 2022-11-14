package com.xjhqre.portal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjhqre.common.domain.admin.Menu;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.portal.mapper.MenuMapper;
import com.xjhqre.portal.service.MenuService;

/**
 * 菜单 业务层处理
 *
 * @author xjhqre
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 查询用户权限信息
     *
     * @param menu
     *            菜单信息
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuList(Menu menu, Long userId) {
        List<Menu> menuList = null;
        // 管理员显示所有菜单信息
        if (User.isSuperAdmin(userId)) {
            menuList = this.menuMapper.selectList(null);
        } else {
            menu.getParams().put("userId", userId);
            menuList = this.menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId
     *            用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = this.menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId
     *            角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = this.menuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId
     *            角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Menu> selectMenuListByRoleId(Long roleId) {
        return this.menuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus
     *            菜单列表
     * @return 树结构列表
     */
    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>(); // 存放所有权限的id
        for (Menu menu : menus) {
            tempList.add(menu.getMenuId());
        }
        for (Menu menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                this.recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId
     *            菜单ID
     * @return 菜单信息
     */
    @Override
    public Menu selectMenuById(Long menuId) {
        return this.menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = this.menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list
     *            分类表
     * @param parentId
     *            传入的父节点ID
     * @return String
     */
    public List<Menu> getChildPerms(List<Menu> list, int parentId) {
        List<Menu> returnList = new ArrayList<Menu>();
        for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
            Menu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                this.recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param allMenuList
     *            权限列表
     * @param parent
     *            上一级权限
     */
    private void recursionFn(List<Menu> allMenuList, Menu parent) {
        // 得到 t 的直接子节点列表
        List<Menu> childList = this.getChildList(allMenuList, parent);
        parent.setChildren(childList);
        for (Menu tChild : childList) {
            // 判断 tChild 是否还有子节点
            if (!this.getChildList(allMenuList, tChild).isEmpty()) {
                // 递归
                this.recursionFn(allMenuList, tChild);
            }
        }
    }

    /**
     * 得到当前节点 t 的直接子节点列表
     */
    private List<Menu> getChildList(List<Menu> list, Menu t) {
        List<Menu> tlist = new ArrayList<>();
        for (Menu child : list) {
            if (child.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(child);
            }
        }
        return tlist;
    }

}
