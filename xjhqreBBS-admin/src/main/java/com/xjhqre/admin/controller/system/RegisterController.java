package com.xjhqre.admin.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.admin.core.BaseController;
import com.xjhqre.admin.service.ConfigService;
import com.xjhqre.admin.service.RegisterService;
import com.xjhqre.common.common.R;
import com.xjhqre.common.domain.model.RegisterBody;
import com.xjhqre.common.utils.StringUtils;

import io.swagger.annotations.Api;

/**
 * 注册验证
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "用户注册接口", tags = "用户注册接口")
public class RegisterController extends BaseController {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private ConfigService configService;

    @PostMapping("/register")
    public R<String> register(@RequestBody RegisterBody user) {
        if (!("true".equals(this.configService.selectConfigByKey("registerUser")))) {
            return R.error("当前系统没有开启注册功能！");
        }
        String msg = this.registerService.register(user);
        return StringUtils.isEmpty(msg) ? R.success("注册成功") : R.error(msg);
    }
}
