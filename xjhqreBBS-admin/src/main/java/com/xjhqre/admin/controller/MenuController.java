// package com.xjhqre.admin.controller;
//
// import java.util.List;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.ruoyi.common.annotation.Log;
// import com.ruoyi.common.constant.UserConstants;
// import com.ruoyi.common.core.domain.entity.SysMenu;
// import com.ruoyi.common.enums.BusinessType;
// import com.ruoyi.common.utils.StringUtils;
// import com.ruoyi.system.service.ISysMenuService;
// import com.xjhqre.admin.core.BaseController;
// import com.xjhqre.common.common.R;
// import com.xjhqre.common.domain.entity.Menu;
//
/// **
// * 菜单信息
// *
// * @author xjhqre
// */
// @RestController
// @RequestMapping("/menu")
// public class MenuController extends BaseController {
// @Autowired
// private ISysMenuService menuService;
//
// /**
// * 获取菜单列表
// */
// @PreAuthorize("@ss.hasPermission('system:menu:list')")
// @GetMapping("/list")
// public R<List<Menu>> list(Menu menu) {
// List<Menu> menus = this.menuService.selectMenuList(menu, this.getUserId());
// return R.success(menus);
// }
//
// /**
// * 根据菜单编号获取详细信息
// */
// @PreAuthorize("@ss.hasPermission('system:menu:query')")
// @GetMapping(value = "/{menuId}")
// public R getInfo(@PathVariable Long menuId) {
// return R.success(this.menuService.selectMenuById(menuId));
// }
//
// /**
// * 获取菜单下拉树列表
// */
// @GetMapping("/treeselect")
// public R treeselect(SysMenu menu) {
// List<SysMenu> menus = this.menuService.selectMenuList(menu, this.getUserId());
// return R.success(this.menuService.buildMenuTreeSelect(menus));
// }
//
// /**
// * 加载对应角色菜单列表树
// */
// @GetMapping(value = "/roleMenuTreeselect/{roleId}")
// public R roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
// List<SysMenu> menus = this.menuService.selectMenuList(this.getUserId());
// R ajax = R.success();
// ajax.put("checkedKeys", this.menuService.selectMenuListByRoleId(roleId));
// ajax.put("menus", this.menuService.buildMenuTreeSelect(menus));
// return ajax;
// }
//
// /**
// * 新增菜单
// */
// @PreAuthorize("@ss.hasPermission('system:menu:add')")
// @Log(title = "菜单管理", businessType = BusinessType.INSERT)
// @PostMapping
// public R add(@Validated @RequestBody SysMenu menu) {
// if (UserConstants.NOT_UNIQUE.equals(this.menuService.checkMenuNameUnique(menu))) {
// return R.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
// } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
// return R.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
// }
// menu.setCreateBy(this.getUsername());
// return toAjax(this.menuService.insertMenu(menu));
// }
//
// /**
// * 修改菜单
// */
// @PreAuthorize("@ss.hasPermission('system:menu:edit')")
// @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
// @PutMapping
// public R edit(@Validated @RequestBody SysMenu menu) {
// if (UserConstants.NOT_UNIQUE.equals(this.menuService.checkMenuNameUnique(menu))) {
// return R.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
// } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
// return R.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
// } else if (menu.getMenuId().equals(menu.getParentId())) {
// return R.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
// }
// menu.setUpdateBy(this.getUsername());
// return toAjax(this.menuService.updateMenu(menu));
// }
//
// /**
// * 删除菜单
// */
// @PreAuthorize("@ss.hasPermission('system:menu:remove')")
// @Log(title = "菜单管理", businessType = BusinessType.DELETE)
// @DeleteMapping("/{menuId}")
// public R remove(@PathVariable("menuId") Long menuId) {
// if (this.menuService.hasChildByMenuId(menuId)) {
// return R.error("存在子菜单,不允许删除");
// }
// if (this.menuService.checkMenuExistRole(menuId)) {
// return R.error("菜单已分配,不允许删除");
// }
// return toAjax(this.menuService.deleteMenuById(menuId));
// }
// }