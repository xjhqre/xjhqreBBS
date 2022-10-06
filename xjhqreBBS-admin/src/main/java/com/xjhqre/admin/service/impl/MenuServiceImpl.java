package com.xjhqre.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xjhqre.admin.mapper.MenuMapper;
import com.xjhqre.admin.mapper.RoleMapper;
import com.xjhqre.admin.mapper.RoleMenuMapper;
import com.xjhqre.admin.service.MenuService;
import com.xjhqre.common.domain.entity.Menu;
import com.xjhqre.common.domain.entity.Role;
import com.xjhqre.common.domain.entity.User;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;

/**
 * 菜单 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MenuServiceImpl implements MenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 根据用户查询系统菜单列表
     * 
     * @param userId
     *            用户ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuList(Long userId) {
        return this.selectMenuList(new Menu(), userId);
    }

    /**
     * 查询系统菜单列表
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
            menuList = this.menuMapper.selectMenuList(menu);
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
     * 根据用户ID查询菜单
     * 
     * @param userId
     *            用户名称
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        if (SecurityUtils.isSuperAdmin(userId)) {
            menus = this.menuMapper.selectMenuTreeAll();
        } else {
            menus = this.menuMapper.selectMenuTreeByUserId(userId);
        }
        return this.getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId
     *            角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        Role role = this.roleMapper.selectRoleById(roleId);
        return this.menuMapper.selectMenuListByRoleId(roleId, false);
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
        List<Menu> returnList = new ArrayList<Menu>();
        List<Long> tempList = new ArrayList<Long>(); // 存放所有权限的id
        for (Menu dept : menus) {
            tempList.add(dept.getMenuId());
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
     * 查询权限是否被使用，用于删除权限时校验
     * 
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = this.roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 新增保存菜单信息
     * 
     * @param menu
     *            菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(Menu menu) {
        return this.menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     * 
     * @param menu
     *            菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(Menu menu) {
        return this.menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     * 
     * @param menuId
     *            菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return this.menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu
     *            菜单信息
     * @return 结果
     */
    @Override
    public Boolean checkMenuNameUnique(Menu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        Menu info = this.menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        return !StringUtils.isNotNull(info) || info.getMenuId().longValue() == menuId.longValue();
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
            Menu t = (Menu)iterator.next();
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
     * @param list
     *            权限列表
     * @param t
     *            顶级权限
     */
    private void recursionFn(List<Menu> list, Menu t) {
        // 得到 t 的直接子节点列表
        List<Menu> childList = this.getChildList(list, t);
        t.setChildren(childList);
        for (Menu tChild : childList) {
            // 判断 tChild 是否还有子节点
            if (this.hasChild(list, tChild)) {
                // 递归
                this.recursionFn(list, tChild);
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

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Menu> list, Menu t) {
        return this.getChildList(list, t).size() > 0;
    }

}
