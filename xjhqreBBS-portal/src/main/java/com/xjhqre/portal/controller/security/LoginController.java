package com.xjhqre.portal.controller.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.model.LoginBody;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.portal.security.service.LoginService;
import com.xjhqre.portal.security.service.PermissionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登录验证
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "用户登陆接口", tags = "用户登陆接口")
@RequestMapping("/portal")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 登录方法
     * 
     * @param loginBody
     *            登录信息
     * @return token
     */
    @ApiOperation(value = "登陆方法")
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String token = this.loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
            loginBody.getUuid());
        return R.success("登陆成功").add(Constants.TOKEN, token);
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    public R<String> getInfo() {
        User user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = this.permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = this.permissionService.getMenuPermission(user);
        return R.success("获取信息成功").add("user", user).add("roles", roles).add("permissions", permissions);
    }

}
