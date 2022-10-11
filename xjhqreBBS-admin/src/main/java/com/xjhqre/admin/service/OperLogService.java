package com.xjhqre.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.domain.OperLog;

/**
 * 操作日志 服务层
 * 
 * @author xjhqre
 */
public interface OperLogService {

    /**
     * 根据条件分页查询操作日志记录
     * 
     * @param operLog
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<OperLog> listOperLog(OperLog operLog, Integer pageNum, Integer pageSize);

    /**
     * 新增操作日志
     * 
     * @param operLog
     *            操作日志对象
     */
    void insertOperLog(OperLog operLog);

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog
     *            操作日志对象
     * @return 操作日志集合
     */
    List<OperLog> selectOperLogList(OperLog operLog);

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds
     *            需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     * 
     * @param operId
     *            操作ID
     * @return 操作日志对象
     */
    OperLog selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
