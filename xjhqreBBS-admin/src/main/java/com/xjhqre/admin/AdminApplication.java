package com.xjhqre.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lhr on 17-7-31.
 */
@SpringBootApplication(scanBasePackages = {"com.xjhqre.common.*", "com.xjhqre.admin.*", "com.xjhqre.quartz.*"})
public class AdminApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(AdminApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            // （推荐）如果项目中存在日志框架，可以通过日志框架打印
            LOGGER.debug("the exception is {}", e.getMessage(), e);
        }
    }
}
