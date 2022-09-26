package com.xjhqre.common.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.xjhqre.common.exception.ErrorEnume;
import com.xjhqre.common.exception.MyServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: xjhqre
 * @DateTime: 2022/4/23 20:04
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    // @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    // public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
    // log.error(ex.getMessage());
    //
    // if (ex.getMessage().contains("Duplicate entry")) {
    // String[] split = ex.getMessage().split(" ");
    // String msg = split[2] + "已存在";
    // return R.error(msg);
    // }
    //
    // return R.error("未知错误");
    // }

    /**
     * 统一处理参数校验异常
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<String> handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型：{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        R<String> r = R.error(ErrorEnume.VALID_EXCEPTION.getCode(), ErrorEnume.VALID_EXCEPTION.getMsg());
        r.add("data", errorMap);
        return r;
    }

    /**
     * 我的服务异常处理
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(value = MyServiceException.class)
    public R<String> handleMyServiceException(MyServiceException e) {
        log.error("系统出现异常，异常编号：{}， 异常信息：{}", e.getCode(), e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return R.error("系统未知异常");
    }
}