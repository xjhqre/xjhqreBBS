package com.xjhqre.admin.controller;

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
import com.xjhqre.admin.entity.dto.AdminDTO;
import com.xjhqre.admin.entity.vo.AdminVO;
import com.xjhqre.admin.service.AdminService;
import com.xjhqre.common.common.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequiresRoles(value = {"1"})
@RequestMapping("/admin")
@Api(value = "管理员操作接口", tags = {"管理员操作接口"})
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    /**
     * 分页查询管理员列表
     * 
     * @param adminDTO
     * @param size
     * @param current
     * @return
     */
    @ApiOperation("分页查询管理员列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", value = "正整数，表示每页几条记录", required = true, dataType = "int", example = "20"),
        @ApiImplicitParam(name = "current", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1")})
    @GetMapping("findAdminUser/{size}/{current}")
    public R<IPage<AdminVO>> findAdminUser(AdminDTO adminDTO, @PathVariable("size") Integer size,
        @PathVariable("current") Integer current) {
        return R.success(this.adminService.findAdmin(adminDTO, current, size));
    }

    /**
     * 添加管理员
     * 
     * @param adminDTO
     * @return
     */
    @ApiOperation(value = "添加管理员")
    @PostMapping("/addAdmin")
    public R<String> addAdmin(AdminDTO adminDTO) {
        this.adminService.saveAdmin(adminDTO);
        return R.success("添加管理员成功");
    }

    /**
     * 删除管理员
     * 
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除管理员")
    @PostMapping("/delete")
    public R<String> deleteAdmin(@RequestParam(value = "ids") Long[] ids) {

        this.adminService.deleteAdminByIds(ids);
        return R.success("删除管理员成功");
    }

    /**
     * 添加管理员角色
     * 
     * @param uid
     * @param roleIds
     * @return
     */
    @ApiOperation(value = "给管理员添加角色")
    @PostMapping("/saveAdminRoles")
    public R<String> saveAdminRoles(Long uid, Long[] roleIds) {
        this.adminService.saveAdminRoles(uid, roleIds);
        return R.success("添加角色成功");
    }

    /**
     * 启用管理员
     * 
     * @param ids
     * @return
     */
    @ApiOperation(value = "启用管理员")
    @PostMapping("/enableAdmin")
    public R<String> enableAdmin(@RequestParam(value = "ids") Long[] ids) {
        this.adminService.enableAdmin(ids);
        return R.success("启用管理员成功");
    }

    /**
     * 禁用管理员
     * 
     * @param ids
     * @return
     */
    @ApiOperation(value = "禁用管理员")
    @PostMapping("/disableAdmin")
    public R<String> disableAdmin(@RequestParam(value = "ids") Long[] ids) {
        this.adminService.disableAdmin(ids);
        return R.success("禁用管理员成功");
    }
}
