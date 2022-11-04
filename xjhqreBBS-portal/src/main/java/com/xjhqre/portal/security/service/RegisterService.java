package com.xjhqre.portal.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.model.RegisterBody;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.manager.AsyncFactory;
import com.xjhqre.common.manager.AsyncManager;
import com.xjhqre.common.service.UserService;
import com.xjhqre.common.utils.SecurityUtils;

/**
 * 注册校验方法
 * 
 * @author xjhqre
 */
@Component
public class RegisterService {
    @Autowired
    private UserService userService;

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        User user = new User();
        user.setUserName(username);

        // TODO 验证码开关
        // boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        // if (captchaEnabled) {
        // this.validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        // }
        if (Constants.NOT_UNIQUE.equals(this.userService.checkUserNameUnique(user))) {
            throw new ServiceException("该用户名已存在");
        } else if (Constants.NOT_UNIQUE.equals(this.userService.checkEmailUnique(user))) {
            throw new ServiceException("该邮箱已存在");
        } else {
            user.setNickName(username);
            user.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = this.userService.registerUser(user);
            if (!regFlag) {
                throw new ServiceException("注册失败，请联系管理人员");
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.REGISTER, "注册成功"));
            }
        }
    }

    // /**
    // * 校验验证码
    // *
    // * @param username
    // * 用户名
    // * @param code
    // * 验证码
    // * @param uuid
    // * 唯一标识
    // */
    // public void validateCaptcha(String username, String code, String uuid) {
    // String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
    // String captcha = this.redisCache.getCacheObject(verifyKey);
    // this.redisCache.deleteObject(verifyKey);
    // if (captcha == null) {
    // throw new ServiceException("验证码已失效");
    // }
    // if (!code.equalsIgnoreCase(captcha)) {
    // throw new ServiceException("验证码已错误");
    // }
    // }
}
