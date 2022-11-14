package com.xjhqre.portal.controller.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.common.common.R;
import com.xjhqre.common.core.BaseController;
import com.xjhqre.common.domain.admin.Menu;
import com.xjhqre.portal.service.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 菜单信息
 * 
 * @author xjhqre
 */
@RestController
@RequestMapping("/system/menu")
@Api(value = "菜单操作接口", tags = "菜单操作接口")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation(value = "根据菜单编号获取详细信息")
    @GetMapping(value = "/{menuId}")
    public R<Menu> getInfo(@PathVariable Long menuId) {
        Menu menu = this.menuService.selectMenuById(menuId);
        return R.success(menu);
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation(value = "获取菜单下拉树列表")
    @GetMapping("/treeSelect")
    public R<List<Menu>> treeSelect(Menu menu) {
        List<Menu> menus = this.menuService.selectMenuList(menu, this.getUserId());
        menus = this.menuService.buildMenuTree(menus);
        return R.success(menus);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation(value = "加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public R<String> roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = this.menuService.selectMenuListByRoleId(roleId);
        menus = this.menuService.buildMenuTree(menus);
        return R.success("加载对应角色菜单列表树成功").add("menus", menus);
    }

}