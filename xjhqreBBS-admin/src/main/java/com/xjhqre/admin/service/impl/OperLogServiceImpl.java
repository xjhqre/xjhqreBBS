package com.xjhqre.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.OperLogMapper;
import com.xjhqre.admin.service.OperLogService;
import com.xjhqre.common.domain.OperLog;

/**
 * 操作日志 服务层处理
 * 
 * @author xjhqre
 */
@Service
public class OperLogServiceImpl implements OperLogService {
    @Autowired
    private OperLogMapper operLogMapper;

    /**
     * 根据条件分页查询操作日志记录
     * 
     * @param operLog
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<OperLog> listOperLog(OperLog operLog, Integer pageNum, Integer pageSize) {
        return this.operLogMapper.listOperLog(new Page<OperLog>(pageNum, pageSize), operLog);
    }

    /**
     * 新增操作日志
     * 
     * @param operLog
     *            操作日志对象
     */
    @Override
    public void insertOperLog(OperLog operLog) {
        this.operLogMapper.insertOperLog(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog
     *            操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<OperLog> selectOperLogList(OperLog operLog) {
        return this.operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds
     *            需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return this.operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId
     *            操作ID
     * @return 操作日志对象
     */
    @Override
    public OperLog selectOperLogById(Long operId) {
        return this.operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        this.operLogMapper.cleanOperLog();
    }
}
