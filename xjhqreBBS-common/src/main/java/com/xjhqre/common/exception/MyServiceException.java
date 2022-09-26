package com.xjhqre.common.exception;

import lombok.Data;

/**
 * @Author: xjhqre
 * @DateTime: 2022/6/18 14:52
 */
@Data
public class MyServiceException extends RuntimeException {
    // 异常编号
    int code;

    public MyServiceException(String message) {
        super(message);
    }

    public MyServiceException(ErrorEnume errorEnume) {
        super(errorEnume.getMsg());
        this.code = errorEnume.getCode();
    }
}