package com.xjhqre.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.admin.core.BaseController;
import com.xjhqre.admin.entity.Permission;
import com.xjhqre.admin.entity.dto.PermissionDTO;
import com.xjhqre.admin.entity.vo.PermissionVO;
import com.xjhqre.admin.service.PermissionService;
import com.xjhqre.admin.shiro.ShiroService;
import com.xjhqre.common.common.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/permission")
@Api(value = "权限操作接口", tags = {"权限操作接口"})
public class PermissionController extends BaseController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 加载菜单权限
     * 
     * @return
     */
    @ApiOperation(value = "查询当前用户菜单权限")
    @PostMapping("/loadMenu")
    public List<Permission> loadMenu() {
        Long userid = (Long)SecurityUtils.getSubject().getSession().getAttribute("AdminSessionId");
        return this.permissionService.loadUserPermissionByType(userid, 1);
    }

    @ApiOperation(value = "查询当前用户所有权限")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "正整数，表示每页几条记录", required = true, dataType = "int", example = "20"),
        @ApiImplicitParam(name = "current", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1")})
    @GetMapping("listPermission/{size}/{current}")
    public R<IPage<PermissionVO>> listPermission(PermissionDTO permissionDTO, @PathVariable("size") Integer size,
        @PathVariable("current") Integer current) {
        return R.success(this.permissionService.findPermission(permissionDTO, current, size));
    }

    @RequiresRoles(value = {"1"}) // 只有超级管理员能添加
    @ApiOperation(value = "添加权限")
    @PostMapping("/add")
    public R<String> add(@Valid PermissionDTO permission) {

        this.permissionService.save(permission);
        // 更新权限
        this.shiroService.updatePermission();
        return R.success("添加权限成功");
    }

    @ApiOperation(value = "删除权限")
    @PostMapping("/delete")
    public R<String> delete(@RequestParam(value = "ids") Permission[] ids) {
        this.permissionService.deleteByIds(ids);
        // 更新权限
        this.shiroService.updatePermission();
        return R.success("删除权限成功");
    }

}
