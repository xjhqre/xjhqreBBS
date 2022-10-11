package com.xjhqre.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.Menu;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.common.service.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @ApiOperation(value = "分页查询菜单列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findMenu/{pageNum}/{pageSize}")
    @PreAuthorize("@ss.hasPermission('system:menu:list')")
    public R<IPage<Menu>> findMenu(Menu menu, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.menuService.findMenu(menu, pageNum, pageSize));
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation(value = "根据菜单编号获取详细信息")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public R<Menu> getInfo(@PathVariable Long menuId) {
        Menu menu = this.menuService.selectMenuById(menuId);
        return R.success(menu);
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation(value = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public R<List<Menu>> treeselect(Menu menu) {
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
        List<Menu> menus = this.menuService.selectMenuList(this.getUserId());
        List<Long> roleIds = this.menuService.selectMenuListByRoleId(roleId);
        menus = this.menuService.buildMenuTree(menus);
        return R.success("加载对应角色菜单列表树成功").add("roleIds", roleIds).add("menus", menus);
    }

    /**
     * 新增菜单
     */
    @ApiOperation(value = "新增菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody Menu menu) {
        if (Constants.NOT_UNIQUE.equals(this.menuService.checkMenuNameUnique(menu))) {
            return R.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setCreateBy(this.getUsername());
        this.menuService.insertMenu(menu);
        return R.success("新增菜单成功");
    }

    /**
     * 修改菜单
     */
    @ApiOperation(value = "修改菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody Menu menu) {
        if (Constants.NOT_UNIQUE.equals(this.menuService.checkMenuNameUnique(menu))) {
            return R.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return R.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(this.getUsername());
        this.menuService.updateMenu(menu);
        return R.success("修改菜单");
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public R<String> remove(@PathVariable("menuId") Long menuId) {
        if (this.menuService.hasChildByMenuId(menuId)) {
            return R.error("存在子菜单,不允许删除");
        }
        if (this.menuService.checkMenuExistRole(menuId)) {
            return R.error("菜单已分配,不允许删除");
        }
        this.menuService.deleteMenuById(menuId);
        return R.success("删除菜单成功");
    }
}