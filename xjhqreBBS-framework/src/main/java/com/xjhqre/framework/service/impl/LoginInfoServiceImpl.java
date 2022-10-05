package com.xjhqre.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xjhqre.common.domain.LoginInfo;
import com.xjhqre.framework.mapper.LoginInfoMapper;
import com.xjhqre.framework.service.LoginInfoService;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    /**
     * 新增系统登录日志
     * 
     * @param loginInfo
     *            访问日志对象
     */
    @Override
    public void insertLoginInfo(LoginInfo loginInfo) {
        this.loginInfoMapper.insertLogininfor(loginInfo);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param loginInfo
     *            访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<LoginInfo> selectLoginInfoList(LoginInfo loginInfo) {
        return this.loginInfoMapper.selectLogininforList(loginInfo);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds
     *            需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLoginInfoByIds(Long[] infoIds) {
        return this.loginInfoMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginInfo() {
        this.loginInfoMapper.cleanLogininfor();
    }
}
