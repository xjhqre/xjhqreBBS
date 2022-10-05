package com.xjhqre.common.domain.model;

import lombok.Data;

/**
 * 用户登录注册对象
 * 
 * @author xjhqre
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
