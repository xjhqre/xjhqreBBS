package com.xjhqre.quartz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.quartz.domain.QuartzJob;

/**
 * 调度任务信息 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface JobMapper {
    /**
     * 查询调度任务日志集合
     * 
     * @param QuartzJob
     *            调度信息
     * @return 操作日志集合
     */
    List<QuartzJob> selectJobList(QuartzJob QuartzJob);

    /**
     * 根据条件分页查询调度任务
     * 
     * @param quartzJobPage
     * @param quartzJob
     * @return
     */
    IPage<QuartzJob> findJob(@Param("quartzJobPage") Page<QuartzJob> quartzJobPage,
        @Param("quartzJob") QuartzJob quartzJob);

    /**
     * 查询所有调度任务
     * 
     * @return 调度任务列表
     */
    List<QuartzJob> selectJobAll();

    /**
     * 通过调度ID查询调度任务信息
     * 
     * @param jobId
     *            调度ID
     * @return 角色对象信息
     */
    QuartzJob selectJobById(Long jobId);

    /**
     * 通过调度ID删除调度任务信息
     * 
     * @param jobId
     *            调度ID
     * @return 结果
     */
    int deleteJobById(Long jobId);

    /**
     * 批量删除调度任务信息
     * 
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    int deleteJobByIds(Long[] ids);

    /**
     * 修改调度任务信息
     * 
     * @param QuartzJob
     *            调度任务信息
     * @return 结果
     */
    int updateJob(QuartzJob QuartzJob);

    /**
     * 新增调度任务信息
     * 
     * @param QuartzJob
     *            调度任务信息
     * @return 结果
     */
    int insertJob(QuartzJob QuartzJob);
}
