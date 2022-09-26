package com.xjhqre.admin;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.xjhqre.admin.controller.LoginController;
import com.xjhqre.admin.entity.dto.AdminDTO;
import com.xjhqre.admin.service.AdminService;
import com.xjhqre.admin.service.PermissionService;
import com.xjhqre.admin.service.RoleService;
import com.xjhqre.admin.service.UserService;

/**
 * Created by lhr on 17-7-31. user.setUsername("lhr"); user.setPassword("root");
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AdminApplication.class)
public class AdminApplicationTest {

    @Autowired
    DataSource dataSource;

    @Autowired(required = true)
    AdminService adminService;

    @Autowired(required = true)
    PermissionService permissionService;

    @Autowired(required = true)
    RoleService roleService;

    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    /**
     * 添加超级管理员
     */
    @Test
    public void test1() {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setUserName("xjhqre");
        adminDTO.setPassWord("123456");
        this.adminService.saveAdmin(adminDTO);
    }

    @Test
    public void logTest() {
        LOGGER.trace("======trace");
        LOGGER.debug("======debug");
        LOGGER.info("======info");
        LOGGER.warn("======warn");
        LOGGER.error("======error");
    }
}
