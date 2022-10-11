package com.xjhqre.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.constant.UserConstants;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.model.RegisterBody;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.manager.AsyncFactory;
import com.xjhqre.common.manager.AsyncManager;
import com.xjhqre.common.service.ConfigService;
import com.xjhqre.common.service.UserService;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;

/**
 * 注册校验方法
 * 
 * @author xjhqre
 */
@Component
public class RegisterService {
    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        User sysUser = new User();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            this.validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
            || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
            || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (Constants.NOT_UNIQUE.equals(this.userService.checkUserNameUnique(sysUser))) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = this.userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.REGISTER, "注册成功"));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     * 
     * @param username
     *            用户名
     * @param code
     *            验证码
     * @param uuid
     *            唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = this.redisCache.getCacheObject(verifyKey);
        this.redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new ServiceException("验证码已失效");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new ServiceException("验证码已错误");
        }
    }
}
