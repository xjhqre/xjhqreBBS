package com.xjhqre.admin.core;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.util.WebUtils;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.xjhqre.admin.entity.Admin;

/**
 * 用于获取登陆用户数据
 */
@Controller
public class BaseController {

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    public Admin getAdminByHeader(ServletRequest request) throws Exception {
        // 前端ajax的headers中必须传入Authorization的值
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        Session session = this.redisSessionDAO.readSession(id);
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        SimplePrincipalCollection coll = (SimplePrincipalCollection)obj;
        String userStr = JSON.toJSON(coll.getPrimaryPrincipal()).toString();
        return JSON.parseObject(userStr, Admin.class);
    }

}