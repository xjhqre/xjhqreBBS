package com.xjhqre.sms;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * SmsApplication
 * </p>
 *
 * @author xjhqre
 * @since 11月 04, 2022
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableRabbit
@ComponentScan(basePackages = {"com.xjhqre.common.*", "com.xjhqre.sms.*"})
public class SmsApplication {}
