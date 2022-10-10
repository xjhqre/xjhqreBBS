package com.xjhqre.admin.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.ConfigMapper;
import com.xjhqre.admin.service.ConfigService;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.constant.UserConstants;
import com.xjhqre.common.domain.entity.Config;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.text.Convert;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;

/**
 * 参数配置 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        this.loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     * 
     * @param configId
     *            参数配置ID
     * @return 参数配置信息
     */
    @Override
    public Config selectConfigById(Long configId) {
        Config config = new Config();
        config.setConfigId(configId);
        return this.configMapper.selectConfig(config);
    }

    @Override
    public IPage<Config> listConfig(Config config, Integer pageNum, Integer pageSize) {
        return this.configMapper.listConfig(new Page<>(pageNum, pageSize), config);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey
     *            参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(this.redisCache.getCacheObject(this.getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        Config config = new Config();
        config.setConfigKey(configKey);
        Config retConfig = this.configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            this.redisCache.setCacheObject(this.getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = this.selectConfigByKey("captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     * 
     * @param config
     *            参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<Config> selectConfigList(Config config) {
        return this.configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config
     *            参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(Config config) {
        int row = this.configMapper.insertConfig(config);
        if (row > 0) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     * 
     * @param config
     *            参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(Config config) {
        int row = this.configMapper.updateConfig(config);
        if (row > 0) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     * 
     * @param configIds
     *            需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            Config config = this.selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            this.configMapper.deleteConfigById(configId);
            this.redisCache.deleteObject(this.getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<Config> configsList = this.configMapper.selectConfigList(new Config());
        for (Config config : configsList) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        Collection<String> keys = this.redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        this.redisCache.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        this.clearConfigCache();
        this.loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config
     *            参数配置信息
     * @return 结果
     */
    @Override
    public Boolean checkConfigKeyUnique(Config config) {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        Config info = this.configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey
     *            参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
