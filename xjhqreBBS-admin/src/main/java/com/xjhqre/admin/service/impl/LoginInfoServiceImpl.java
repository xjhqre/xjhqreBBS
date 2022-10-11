package com.xjhqre.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.LoginInfoMapper;
import com.xjhqre.admin.service.LoginInfoService;
import com.xjhqre.common.domain.LoginInfo;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author xjhqre
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    /**
     * 根据条件分页查询登陆信息
     * 
     * @param loginInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<LoginInfo> listLoginInfo(LoginInfo loginInfo, Integer pageNum, Integer pageSize) {
        return this.loginInfoMapper.listLoginInfo(new Page<>(pageNum, pageSize), loginInfo);
    }

    /**
     * 新增系统登录日志
     * 
     * @param loginInfo
     *            访问日志对象
     */
    @Override
    public void insertLoginInfo(LoginInfo loginInfo) {
        this.loginInfoMapper.insertLoginInfo(loginInfo);
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
        return this.loginInfoMapper.selectLoginInfoList(loginInfo);
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
        return this.loginInfoMapper.deleteLoginInfoByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginInfo() {
        this.loginInfoMapper.cleanLoginInfo();
    }
}
