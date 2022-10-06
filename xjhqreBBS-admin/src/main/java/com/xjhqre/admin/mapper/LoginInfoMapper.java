package com.xjhqre.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xjhqre.common.domain.LoginInfo;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface LoginInfoMapper {
    /**
     * 新增系统登录日志
     * 
     * @param loginInfo
     *            访问日志对象
     */
    void insertLoginInfo(@Param("loginInfo") LoginInfo loginInfo);

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
     * 
     * @return 结果
     */
    int cleanLoginInfo();
}
