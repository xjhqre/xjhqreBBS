package com.xjhqre.quartz.util;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.constant.ScheduleConstants;
import com.xjhqre.common.utils.BeanUtils;
import com.xjhqre.common.utils.ExceptionUtil;
import com.xjhqre.common.utils.SpringUtils;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.quartz.domain.JobLog;
import com.xjhqre.quartz.domain.QuartzJob;
import com.xjhqre.quartz.service.JobLogService;

/**
 * 抽象quartz调用
 *
 * @author ruoyi
 */
public abstract class AbstractQuartzJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        QuartzJob quartzJob = new QuartzJob();
        BeanUtils.copyBeanProp(quartzJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            this.before(); // 设置执行开始时间，记录任务执行花费的实现
            this.doExecute(context, quartzJob); // 实现类执行的具体方法，即需要执行的任务
            this.after(quartzJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            this.after(quartzJob, e);
        }
    }

    /**
     * 执行前
     *
     */
    protected void before() {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param quartzJob
     *            系统计划任务
     */
    protected void after(QuartzJob quartzJob, Exception e) {
        Date startTime = threadLocal.get(); // 获取任务执行开始时间
        threadLocal.remove(); // 移除开始时间

        // 记录日志
        final JobLog jobLog = new JobLog();
        jobLog.setJobName(quartzJob.getJobName());
        jobLog.setJobGroup(quartzJob.getJobGroup());
        jobLog.setInvokeTarget(quartzJob.getInvokeTarget());
        jobLog.setStartTime(startTime);
        jobLog.setStopTime(new Date());
        long runMs = jobLog.getStopTime().getTime() - jobLog.getStartTime().getTime();
        jobLog.setJobMessage(jobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            jobLog.setStatus(Constants.FAIL);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            jobLog.setExceptionInfo(errorMsg);
        } else {
            jobLog.setStatus(Constants.SUCCESS);
        }

        // 写入数据库当中
        SpringUtils.getBean(JobLogService.class).addJobLog(jobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context
     *            工作执行上下文对象
     * @param sysJob
     *            系统计划任务
     * @throws Exception
     *             执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, QuartzJob sysJob) throws Exception;
}
