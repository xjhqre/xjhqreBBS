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
import com.xjhqre.common.domain.admin.Role;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.admin.UserRole;
import com.xjhqre.common.domain.model.LoginUser;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.common.security.service.PermissionService;
import com.xjhqre.common.security.service.TokenService;
import com.xjhqre.common.service.RoleService;
import com.xjhqre.common.service.UserService;
import com.xjhqre.common.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 角色信息
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "角色操作接口", tags = "角色操作接口")
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "分页查询角色列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findRole/{pageNum}/{pageSize}")
    @PreAuthorize("@ss.hasPermission('system:role:list')")
    public R<IPage<Role>> findRole(Role role, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.roleService.findRole(role, pageNum, pageSize));
    }

    /**
     * 根据角色编号获取详细信息
     */
    @ApiOperation(value = "根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public R<Role> getInfo(@PathVariable Long roleId) {
        Role role = this.roleService.selectRoleById(roleId);
        return R.success(role);
    }

    /**
     * 新增角色
     */
    @ApiOperation(value = "新增角色")
    @PreAuthorize("@ss.hasPermission('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody Role role) {
        if (Constants.NOT_UNIQUE.equals(this.roleService.checkRoleNameUnique(role))) {
            return R.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (Constants.NOT_UNIQUE.equals(this.roleService.checkRoleKeyUnique(role))) {
            return R.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(this.getUsername());
        this.roleService.insertRole(role);
        return R.success("添加角色成功");

    }

    /**
     * 修改保存角色
     */
    @ApiOperation(value = "修改保存角色")
    @PreAuthorize("@ss.hasPermission('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody Role role) {
        this.roleService.checkRoleAllowed(role.getRoleId());
        if (Constants.NOT_UNIQUE.equals(this.roleService.checkRoleNameUnique(role))) {
            return R.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (Constants.NOT_UNIQUE.equals(this.roleService.checkRoleKeyUnique(role))) {
            return R.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(this.getUsername());

        if (this.roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = this.getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isSuperAdmin()) {
                loginUser.setPermissions(this.permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(this.userService.selectUserByUserName(loginUser.getUser().getUserName()));
                this.tokenService.setLoginUser(loginUser);
            }
            return R.success("修改角色成功");
        }
        return R.error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 状态修改
     */
    @ApiOperation(value = "状态修改")
    @PreAuthorize("@ss.hasPermission('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody Role role) {
        this.roleService.checkRoleAllowed(role.getRoleId());
        role.setUpdateBy(this.getUsername());
        this.roleService.updateRoleStatus(role);
        return R.success("状态修改成功");
    }

    /**
     * 删除角色
     */
    @ApiOperation(value = "删除角色")
    @PreAuthorize("@ss.hasPermission('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public R<String> remove(@PathVariable Long[] roleIds) {
        this.roleService.deleteRoleByIds(roleIds);
        return R.success("删除角色成功");
    }

    /**
     * 获取角色选择框列表
     */
    @ApiOperation(value = "获取角色选择框列表")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    @GetMapping("/optionSelect")
    public R<List<Role>> optionSelect() {
        List<Role> roles = this.roleService.selectRoleAll();
        return R.success(roles);
    }

    @ApiOperation(value = "查询已分配用户角色列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("/authUser/allocatedList/{pageNum}/{pageSize}")
    @PreAuthorize("@ss.hasPermission('system:role:list')")
    public R<IPage<User>> allocatedList(User user, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.userService.selectAllocatedList(user, pageNum, pageSize));
    }

    // /**
    // * 查询未分配用户角色列表
    // */
    // @PreAuthorize("@ss.hasPermission('system:role:list')")
    // @GetMapping("/authUser/unallocatedList")
    // public TableDataInfo unallocatedList(SysUser user) {
    // startPage();
    // List<SysUser> list = this.userService.selectUnallocatedList(user);
    // return getDataTable(list);
    // }

    /**
     * 取消分配给用户的角色
     */
    @ApiOperation(value = "取消分配给用户的角色")
    @PreAuthorize("@ss.hasPermission('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public R<String> cancelAuthUser(@RequestBody UserRole userRole) {
        this.roleService.deleteAuthUser(userRole);
        return R.success("取消角色成功");
    }

    /**
     * 批量取消授权用户
     */
    @ApiOperation(value = "批量取消授权用户")
    @PreAuthorize("@ss.hasPermission('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public R<String> cancelAuthUserAll(Long roleId, Long[] userIds) {
        this.roleService.deleteAuthUsers(roleId, userIds);
        return R.success("批量取消授权成功");
    }

    /**
     * 批量选择用户授权
     */
    @ApiOperation(value = "批量选择用户授权")
    @PreAuthorize("@ss.hasPermission('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public R<String> selectAuthUserAll(Long roleId, Long[] userIds) {
        this.roleService.insertAuthUsers(roleId, userIds);
        return R.success("批量授权成功");
    }
}
