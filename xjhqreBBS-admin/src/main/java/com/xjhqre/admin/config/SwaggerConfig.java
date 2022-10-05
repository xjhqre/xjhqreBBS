package com.xjhqre.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的接口配置
 * 
 * @author ruoyi
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.apiInfo())
            // 是否开启 (true 开启 false隐藏。生产环境建议隐藏)
            // .enable(false)
            .select()
            // 扫描的路径包,设置basePackage会将包下的所有被@Api标记类的所有方法作为api
            // .apis(RequestHandlerSelectors.basePackage("com.mcy.springbootswagger.controller"))
            // 扫描所有接口方法
            .apis(RequestHandlerSelectors.any())
            // 指定路径处理PathSelectors.any()代表所有的路径
            .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            // 设置文档标题(API名称)
            .title("xjhqreBBS接口文档")
            // 文档描述
            .description("xjhqreBBS后台管理系统")
            // 作者信息
            .contact(new Contact("xjhqre", null, null))
            // 版本号
            .version("1.0.0").build();
    }
}
