package com.xjhqre.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by lhr on 17-7-31.
 */
@SpringBootApplication
@EnableCaching // 缓存支持
public class AdminApplication {

    public static void main(String[] args) {
        // 更改properties配置文件名称,避免依赖冲突
        // Properties properties = new Properties();
        // InputStream in = App.class.getClassLoader().getResourceAsStream("admin.properties");
        // properties.load(in);
        // SpringApplication app = new SpringApplication(App.class);
        // app.setDefaultProperties(properties);
        // app.run(args);
        SpringApplication.run(AdminApplication.class, args);
    }
}
