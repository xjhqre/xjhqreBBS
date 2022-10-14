package com.xjhqre.common.controller;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.redis.RedisCache;

import io.swagger.annotations.Api;

@RestController
@Api(value = "邮件服务接口", tags = "邮件服务接口")
public class EmailController {

    @Autowired
    JavaMailSender jms;
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    RedisCache redisCache;

    public static final int EXPIRE_TIME = 5;

    @PostMapping("/sendEmail")
    public R<String> sendEmail(String emailAddress) {
        String reg = "[\\w]+@[\\w]+.[\\w]+[\\w]";
        // 判断输入的字符串是否匹配给定的正则表达式。
        if (!emailAddress.matches(reg)) {
            throw new ServiceException("邮箱地址不合法");
        }
        // 判断redis中是否存在且时间不超过1分钟的验证码
        long expire = this.redisCache.getExpire(CacheConstants.EMAIL_CODE_KEY + emailAddress);
        if (expire > EXPIRE_TIME * 60 - 60) {
            throw new ServiceException("申请验证码过于频繁，请稍后再试");
        }
        // 产生六位随机验证码
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        // 存入redis，过期时间为5分钟
        this.redisCache.setCacheObject(CacheConstants.EMAIL_CODE_KEY + emailAddress, verifyCode, EXPIRE_TIME,
            TimeUnit.MINUTES);

        try {
            // 建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();

            // 发送者
            mainMessage.setFrom(this.sender);

            // 接收者
            mainMessage.setTo(emailAddress);

            // 发送的标题
            mainMessage.setSubject("邮箱验证");

            // 发送的内容
            String msg = "欢迎注册xjhqreBBS论坛用户，您的验证码是：" + verifyCode + "。验证码5分钟后过期。";
            mainMessage.setText(msg);

            // 发送邮件
            this.jms.send(mainMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success("验证码发送成功");
    }
}
