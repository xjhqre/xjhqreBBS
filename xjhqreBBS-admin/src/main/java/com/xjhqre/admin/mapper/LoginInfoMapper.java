package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjhqre.common.domain.LoginInfo;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface LoginInfoMapper extends BaseMapper<LoginInfo> {

    /**
     * 清空系统登录日志
     * 
     * @return 结果
     */
    int cleanLoginInfo();
}
