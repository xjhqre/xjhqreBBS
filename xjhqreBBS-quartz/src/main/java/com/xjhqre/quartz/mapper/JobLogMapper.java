package com.xjhqre.quartz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.quartz.domain.JobLog;

/**
 * 调度任务日志信息 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface JobLogMapper {
    /**
     * 获取quartz调度器日志的计划任务
     * 
     * @param jobLog
     *            调度日志信息
     * @return 调度任务日志集合
     */
    public List<JobLog> selectJobLogList(JobLog jobLog);

    /**
     * 分页查询任务日志
     * 
     * @param jobLogPage
     * @param jobLog
     * @return
     */
    IPage<JobLog> findJobLog(@Param("jobLogPage") Page<JobLog> jobLogPage, @Param("jobLog") JobLog jobLog);

    /**
     * 查询所有调度任务日志
     *
     * @return 调度任务日志列表
     */
    public List<JobLog> selectJobLogAll();

    /**
     * 通过调度任务日志ID查询调度信息
     * 
     * @param jobLogId
     *            调度任务日志ID
     * @return 调度任务日志对象信息
     */
    public JobLog selectJobLogById(Long jobLogId);

    /**
     * 新增任务日志
     * 
     * @param jobLog
     *            调度日志信息
     * @return 结果
     */
    public int insertJobLog(JobLog jobLog);

    /**
     * 批量删除调度日志信息
     * 
     * @param logIds
     *            需要删除的数据ID
     * @return 结果
     */
    public int deleteJobLogByIds(Long[] logIds);

    /**
     * 删除任务日志
     * 
     * @param jobId
     *            调度日志ID
     * @return 结果
     */
    public int deleteJobLogById(Long jobId);

    /**
     * 清空任务日志
     */
    public void cleanJobLog();

}
