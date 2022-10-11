package com.xjhqre.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.Config;

/**
 * 参数配置 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface ConfigMapper {
    /**
     * 查询参数配置信息
     * 
     * @param config
     *            参数配置信息
     * @return 参数配置信息
     */
    Config selectConfig(Config config);

    /**
     * 分页查询配置信息
     * 
     * @param objectPage
     * @param config
     * @return
     */
    IPage<Config> listConfig(@Param("objectPage") Page<Config> objectPage, @Param("config") Config config);

    /**
     * 查询参数配置列表
     * 
     * @param config
     *            参数配置信息
     * @return 参数配置集合
     */
    List<Config> selectConfigList(Config config);

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey
     *            参数键名
     * @return 参数配置信息
     */
    Config checkConfigKeyUnique(String configKey);

    /**
     * 新增参数配置
     * 
     * @param config
     *            参数配置信息
     * @return 结果
     */
    int insertConfig(Config config);

    /**
     * 修改参数配置
     * 
     * @param config
     *            参数配置信息
     * @return 结果
     */
    int updateConfig(Config config);

    /**
     * 删除参数配置
     * 
     * @param configId
     *            参数ID
     * @return 结果
     */
    int deleteConfigById(Long configId);

    /**
     * 批量删除参数信息
     * 
     * @param configIds
     *            需要删除的参数ID
     * @return 结果
     */
    int deleteConfigByIds(Long[] configIds);

}
