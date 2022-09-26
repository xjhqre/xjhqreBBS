/**
 * Copyright (c) 2022, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.entity.AdminRole;
import com.xjhqre.admin.mapper.AdminRoleMapper;
import com.xjhqre.admin.service.AdminRoleService;

/**
 * <p>
 * AdminRoleServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 9月 17, 2022
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {}
