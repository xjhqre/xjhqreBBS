package com.xjhqre.quartz.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.exception.TaskException;
import com.xjhqre.quartz.domain.QuartzJob;

/**
 * 定时任务调度信息信息 服务层
 * 
 * @author ruoyi
 */
public interface JobService {
    /**
     * 获取quartz调度器的计划任务
     * 
     * @param quartzJob
     *            调度信息
     * @return 调度任务集合
     */
    public List<QuartzJob> selectJobList(QuartzJob quartzJob);

    /**
     * 根据条件分页查询调度任务
     * 
     * @param quartzJob
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<QuartzJob> findJob(QuartzJob quartzJob, Integer pageNum, Integer pageSize);

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId
     *            调度任务ID
     * @return 调度任务对象信息
     */
    public QuartzJob selectJobById(Long jobId);

    /**
     * 暂停任务
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    public int pauseJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 恢复任务
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    public int resumeJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    public int deleteJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 批量删除调度信息
     * 
     * @param jobIds
     *            需要删除的任务ID
     * @return 结果
     */
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 任务调度状态修改
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    public int changeStatus(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 立即运行任务
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    public boolean run(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 新增任务
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    int insertJob(QuartzJob quartzJob) throws SchedulerException, TaskException;

    /**
     * 更新任务
     * 
     * @param quartzJob
     *            调度信息
     * @return 结果
     */
    public int updateJob(QuartzJob quartzJob) throws SchedulerException, TaskException;

    /**
     * 校验cron表达式是否有效
     * 
     * @param cronExpression
     *            表达式
     * @return 结果
     */
    public boolean checkCronExpressionIsValid(String cronExpression);

}
