package com.xjhqre.framework.web.service;

import java.util.List;

import com.xjhqre.common.domain.LoginInfo;

/**
 * 系统访问日志情况信息 服务层
 * 
 * @author ruoyi
 */
public interface LoginInforService {
    /**
     * 新增系统登录日志
     * 
     * @param logininfor
     *            访问日志对象
     */
    public void insertLogininfor(LoginInfo logininfor);

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor
     *            访问日志对象
     * @return 登录记录集合
     */
    public List<LoginInfo> selectLogininforList(LoginInfo logininfor);

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds
     *            需要删除的登录日志ID
     * @return 结果
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    public void cleanLogininfor();
}
