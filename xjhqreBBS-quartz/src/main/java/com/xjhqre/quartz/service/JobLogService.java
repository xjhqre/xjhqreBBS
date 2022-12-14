package com.xjhqre.quartz.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.quartz.domain.JobLog;

/**
 * 定时任务调度日志信息信息 服务层
 * 
 * @author ruoyi
 */
public interface JobLogService {
    /**
     * 获取quartz调度器日志的计划任务
     * 
     * @param jobLog
     *            调度日志信息
     * @return 调度任务日志集合
     */
    List<JobLog> selectJobLogList(JobLog jobLog);

    /**
     * 分页查询任务日志
     * 
     * @param jobLog
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<JobLog> findJobLog(JobLog jobLog, Integer pageNum, Integer pageSize);

    /**
     * 通过调度任务日志ID查询调度信息
     * 
     * @param jobLogId
     *            调度任务日志ID
     * @return 调度任务日志对象信息
     */
    JobLog selectJobLogById(Long jobLogId);

    /**
     * 新增任务日志
     * 
     * @param jobLog
     *            调度日志信息
     */
    void addJobLog(JobLog jobLog);

    /**
     * 批量删除调度日志信息
     * 
     * @param logIds
     *            需要删除的日志ID
     * @return 结果
     */
    int deleteJobLogByIds(Long[] logIds);

    /**
     * 删除任务日志
     * 
     * @param jobId
     *            调度日志ID
     * @return 结果
     */
    int deleteJobLogById(Long jobId);

    /**
     * 清空任务日志
     */
    void cleanJobLog();

}
