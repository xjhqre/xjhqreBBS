package com.xjhqre.quartz.util;

import org.quartz.JobExecutionContext;

import com.xjhqre.quartz.domain.QuartzJob;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author ruoyi
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    // 实现类执行的具体方法，即需要执行的任务
    @Override
    protected void doExecute(JobExecutionContext context, QuartzJob quartzJob) throws Exception {
        JobInvokeUtil.invokeMethod(quartzJob);
    }
}
