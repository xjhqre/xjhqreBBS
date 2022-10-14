package com.xjhqre.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author xjhqre
 */
@SpringBootApplication(scanBasePackages = {"com.xjhqre.common.*", "com.xjhqre.portal.*"})
public class PortalApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(PortalApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            // （推荐）如果项目中存在日志框架，可以通过日志框架打印
            LOGGER.debug("the exception is {}", e.getMessage(), e);
        }
    }
}
