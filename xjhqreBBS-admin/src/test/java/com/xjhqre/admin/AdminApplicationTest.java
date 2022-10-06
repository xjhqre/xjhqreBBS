package com.xjhqre.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.xjhqre.admin.security.service.PermissionService;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.admin.service.UserService;
import com.xjhqre.common.domain.entity.User;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.SpringUtils;

/**
 * Created by lhr on 17-7-31. user.setUsername("lhr"); user.setPassword("root");
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AdminApplication.class)
public class AdminApplicationTest {

    @Autowired(required = true)
    PermissionService permissionService;

    @Autowired(required = true)
    RoleService roleService;

    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminApplicationTest.class);

    @org.junit.Test
    public void logTest() {
        LOGGER.trace("======trace");
        LOGGER.debug("======debug");
        LOGGER.info("======info");
        LOGGER.warn("======warn");
        LOGGER.error("======error");
    }

    @org.junit.Test
    public void test2() {
        Object redisCache = SpringUtils.getBean("redisCache");
        System.out.println(redisCache);
    }

    // 新增用户
    @Test
    public void test3() {
        User user = new User();
        user.setUserName("xjhqre");
        user.setNickName("xjhqre");
        user.setCreateBy("xjhqre");
        user.setPassword(SecurityUtils.encryptPassword("123456"));
        this.userService.insertUser(user);
    }
}
