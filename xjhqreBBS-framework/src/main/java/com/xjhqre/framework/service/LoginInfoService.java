package com.xjhqre.framework.service;

import java.util.List;

import com.xjhqre.common.domain.LoginInfo;

/**
 * 系统访问日志情况信息 服务层
 * 
 * @author xjhqre
 */
public interface LoginInfoService {
    /**
     * 新增系统登录日志
     * 
     * @param loginInfo
     *            访问日志对象
     */
    void insertLoginInfo(LoginInfo loginInfo);

    /**
     * 查询系统登录日志集合
     * 
     * @param loginInfo
     *            访问日志对象
     * @return 登录记录集合
     */
    List<LoginInfo> selectLoginInfoList(LoginInfo loginInfo);

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds
     *            需要删除的登录日志ID
     * @return 结果
     */
    int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    void cleanLoginInfo();
}
