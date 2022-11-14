package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjhqre.common.domain.OperLog;

/**
 * 操作日志 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
