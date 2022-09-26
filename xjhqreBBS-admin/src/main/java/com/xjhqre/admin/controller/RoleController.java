package com.xjhqre.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.admin.core.BaseController;
import com.xjhqre.admin.entity.Role;
import com.xjhqre.admin.entity.dto.RoleDTO;
import com.xjhqre.admin.entity.vo.RoleVO;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.common.common.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/role")
@Api(value = "角色操作接口", tags = {"角色操作接口"})
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("分页查询角色列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "正整数，表示每页几条记录", required = true, dataType = "int", example = "20"),
        @ApiImplicitParam(name = "current", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1")})
    @GetMapping("findRole/{size}/{current}")
    public R<IPage<RoleVO>> findRole(RoleDTO roleDTO, @PathVariable("size") Integer size,
        @PathVariable("current") Integer current) {
        return R.success(this.roleService.findRole(roleDTO, current, size));
    }

    @ApiOperation("查询用户所有角色")
    @PostMapping("/listRolesByUserId")
    public R<List<RoleVO>> listRolesByUserId(Long uid) {
        List<RoleVO> roles = this.roleService.listRolesByUserId(uid);
        return R.success(roles);
    }

    @ApiOperation("添加角色")
    @PostMapping("/add")
    public R<String> saveRole(Role role, Long[] permissionIds) {
        this.roleService.saveRole(role, permissionIds);
        return R.success("添加角色成功");
    }

    @ApiOperation("修改角色权限")
    @PostMapping("/addRolePermission)
    public R<String> saveRole(Role role, Long[] permissionIds) {
        this.roleService.saveRole(role, permissionIds);
        return R.success("添加角色成功");
    }

    @ApiOperation("批量删除角色")
    @PostMapping("/delete")
    public R<String> delete(@RequestParam(value = "ids") Long[] ids) {
        this.roleService.deleteRole(ids);
        return R.success("删除角色成功");
    }

}
