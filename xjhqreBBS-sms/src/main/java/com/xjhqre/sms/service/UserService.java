package com.xjhqre.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.admin.User;

/**
 * 用户 业务层
 * 
 * @author xjhqre
 */
public interface UserService extends IService<User> {

    User selectUserById(Long userId);
}
