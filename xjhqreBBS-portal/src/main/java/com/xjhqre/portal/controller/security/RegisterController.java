package com.xjhqre.portal.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.model.RegisterBody;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.service.ConfigService;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.portal.security.service.RegisterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 注册验证
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "用户注册接口", tags = "用户注册接口")
@RequestMapping("/portal")
public class RegisterController extends BaseController {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisCache redisCache;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody @Validated RegisterBody user) {
        if (!("true".equals(this.configService.selectConfigByKey("registerUser")))) {
            return R.error("当前系统没有开启注册功能！");
        }
        String emailCode = this.redisCache.getCacheObject(CacheConstants.EMAIL_CODE_KEY + user.getEmail());
        if (StringUtils.isEmpty(emailCode)) {
            throw new ServiceException("验证码已失效！！！");
        }
        if (!StringUtils.equals(user.getCode(), emailCode)) {
            throw new ServiceException("验证码输入错误！！！");
        }
        // 清除验证码缓存
        this.redisCache.deleteObject(CacheConstants.EMAIL_CODE_KEY + user.getEmail());
        this.registerService.register(user);
        return R.success("注册成功");
    }
}
