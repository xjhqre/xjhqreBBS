package com.xjhqre.sms.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjhqre.common.domain.admin.User;

/**
 * 用户表 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
