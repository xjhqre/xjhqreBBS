package com.xjhqre.portal.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.model.LoginBody;
import com.xjhqre.portal.security.service.LoginService;

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

}
