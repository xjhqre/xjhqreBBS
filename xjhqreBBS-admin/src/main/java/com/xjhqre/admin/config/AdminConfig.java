package com.xjhqre.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描 common 模块里的组件
 */
@Configuration
@ComponentScan(basePackages = "com.xjhqre.common")
public class AdminConfig {}
