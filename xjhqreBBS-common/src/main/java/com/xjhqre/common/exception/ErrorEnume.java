package com.xjhqre.common.exception;

/**
 * @Author: xjhqre
 * @DateTime: 2022/4/23 20:06
 */
public enum ErrorEnume {
    UNKNOW_EXCEPTION(10000, "系统未知异常"), VALID_EXCEPTION(10001, "参数格式校验失败"), EMAIL_DUPLICATE(20000, "邮箱已存在"),
    USERNAME_DUPLICATE(20001, "用户名已存在"), ROLE_DOES_NOT_EXIST(30000, "该用户不存在角色");

    private final int code;
    private final String msg;

    ErrorEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}