package com.xjhqre.picture;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * PictureApplication
 * </p>
 *
 * @author xjhqre
 * @since 10月 24, 2022
 */
@EnableRabbit
@SpringBootApplication(scanBasePackages = {"com.xjhqre.common.*", "com.xjhqre.picture.*"})
public class PictureApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureApplication.class, args);
    }
}
