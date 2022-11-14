package com.xjhqre.portal.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.model.RegisterBody;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.portal.manager.AsyncFactory;
import com.xjhqre.portal.manager.AsyncManager;
import com.xjhqre.portal.service.ConfigService;
import com.xjhqre.portal.service.UserService;

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
    ConfigService configService;
    @Autowired
    RedisCache redisCache;

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        if (!("true".equals(this.configService.selectConfigByKey("registerUser")))) {
            throw new ServiceException("当前系统没有开启注册功能！");
        }
        String emailCode = this.redisCache.getCacheObject(CacheConstants.EMAIL_CODE_KEY + registerBody.getEmail());
        if (StringUtils.isEmpty(emailCode)) {
            throw new ServiceException("验证码已失效！！！");
        }
        if (!StringUtils.equals(registerBody.getCode(), emailCode)) {
            throw new ServiceException("验证码输入错误！！！");
        }
        // 清除验证码缓存
        this.redisCache.deleteObject(CacheConstants.EMAIL_CODE_KEY + registerBody.getEmail());

        User user = new User();
        user.setUserName(registerBody.getUsername());

        if (Constants.NOT_UNIQUE.equals(this.userService.checkUserNameUnique(user))) {
            throw new ServiceException("该用户名已存在");
        } else if (Constants.NOT_UNIQUE.equals(this.userService.checkEmailUnique(user))) {
            throw new ServiceException("该邮箱已存在");
        } else {
            user.setNickName(registerBody.getUsername());
            user.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
            boolean regFlag = this.userService.registerUser(user);
            if (!regFlag) {
                throw new ServiceException("注册失败，请联系管理人员");
            } else {
                AsyncManager.me()
                    .execute(AsyncFactory.recordLoginInfo(registerBody.getUsername(), Constants.REGISTER, "注册成功"));
            }
        }
    }
}
