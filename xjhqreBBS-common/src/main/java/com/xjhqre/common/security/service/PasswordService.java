package com.xjhqre.common.security.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.constant.ErrorCode;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.manager.AsyncFactory;
import com.xjhqre.common.manager.AsyncManager;
import com.xjhqre.common.security.context.AuthenticationContextHolder;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;

/**
 * 登录密码方法
 * 
 * @author xjhqre
 */
@Component
public class PasswordService {
    @Autowired
    private RedisCache redisCache;

    // 密码重试次数
    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    // 锁定时间
    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username
     *            用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(User user) {

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        // 重试次数存放在redis中
        Integer retryCount = this.redisCache.getCacheObject(this.getCacheKey(username));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= this.maxRetryCount) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL,
                StringUtils.format("密码输入错误{0}次，帐户锁定{1}分钟", this.maxRetryCount, this.lockTime)));
            throw new ServiceException(StringUtils.format("密码输入错误{0}次，帐户锁定{1}分钟", this.maxRetryCount, this.lockTime));
        }

        // 如果密码不相同
        if (!this.matches(user, password)) {
            retryCount = retryCount + 1; // 重试次数+1
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL,
                StringUtils.format("密码输入错误{0}次", retryCount)));
            // 更新redis
            this.redisCache.setCacheObject(this.getCacheKey(username), retryCount, this.lockTime, TimeUnit.MINUTES);
            throw new ServiceException(ErrorCode.WRONG_USER_NAME_OR_PASSWORD, "用户名或密码错误");
        } else {
            // 密码相同，清空redis的重试次数
            this.clearLoginRecordCache(username);
        }
    }

    public boolean matches(User user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (this.redisCache.hasKey(this.getCacheKey(loginName))) {
            this.redisCache.deleteObject(this.getCacheKey(loginName));
        }
    }
}