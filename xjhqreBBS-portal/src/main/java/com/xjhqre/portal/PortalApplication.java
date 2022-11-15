package com.xjhqre.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author xjhqre
 */
@SpringBootApplication(scanBasePackages = {"com.xjhqre.common.*", "com.xjhqre.portal.*"})
public class PortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }
}
