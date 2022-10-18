package com.xjhqre.quartz.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.quartz.util.CronUtils;
import com.xjhqre.common.constant.ScheduleConstants;
import com.xjhqre.common.exception.TaskException;
import com.xjhqre.quartz.domain.QuartzJob;
import com.xjhqre.quartz.mapper.JobMapper;
import com.xjhqre.quartz.service.JobService;
import com.xjhqre.quartz.util.ScheduleUtils;

/**
 * 定时任务调度信息 服务层
 * 
 * @author ruoyi
 */
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        this.scheduler.clear();
        List<QuartzJob> jobList = this.jobMapper.selectJobAll();
        for (QuartzJob job : jobList) {
            ScheduleUtils.createScheduleJob(this.scheduler, job);
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     * 
     * @param job
     *            调度信息
     * @return
     */
    @Override
    public List<QuartzJob> selectJobList(QuartzJob job) {
        return this.jobMapper.selectJobList(job);
    }

    @Override
    public IPage<QuartzJob> findJob(QuartzJob quartzJob, Integer pageNum, Integer pageSize) {
        return this.jobMapper.findJob(new Page<>(pageNum, pageSize), quartzJob);
    }

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId
     *            调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public QuartzJob selectJobById(Long jobId) {
        return this.jobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     * 
     * @param job
     *            调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(QuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = this.jobMapper.updateJob(job);
        if (rows > 0) {
            this.scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     * 
     * @param job
     *            调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(QuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = this.jobMapper.updateJob(job);
        if (rows > 0) {
            this.scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param job
     *            调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(QuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = this.jobMapper.deleteJobById(jobId);
        if (rows > 0) {
            this.scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     * 
     * @param jobIds
     *            需要删除的任务ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            QuartzJob job = this.jobMapper.selectJobById(jobId);
            this.deleteJob(job);
        }
    }

    /**
     * 任务调度状态修改
     * 
     * @param job
     *            调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(QuartzJob job) throws SchedulerException {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = this.resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = this.pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     * 
     * @param job
     *            调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(QuartzJob job) throws SchedulerException {
        boolean result = false;
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        QuartzJob properties = this.selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (this.scheduler.checkExists(jobKey)) {
            result = true;
            this.scheduler.triggerJob(jobKey, dataMap);
        }
        return result;
    }

    /**
     * 新增任务
     * 
     * @param job
     *            调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(QuartzJob job) throws SchedulerException, TaskException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue()); // 设置任务状态为暂停
        int rows = this.jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(this.scheduler, job);
        }
        return rows;
    }

    /**
     * 更新任务的时间表达式
     * 
     * @param job
     *            调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(QuartzJob job) throws SchedulerException, TaskException {
        QuartzJob properties = this.selectJobById(job.getJobId());
        int rows = this.jobMapper.updateJob(job);
        if (rows > 0) {
            this.updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务
     * 
     * @param job
     *            任务对象
     * @param jobGroup
     *            任务组名
     */
    public void updateSchedulerJob(QuartzJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (this.scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            this.scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(this.scheduler, job);
    }

    /**
     * 校验cron表达式是否有效
     * 
     * @param cronExpression
     *            表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }
}
