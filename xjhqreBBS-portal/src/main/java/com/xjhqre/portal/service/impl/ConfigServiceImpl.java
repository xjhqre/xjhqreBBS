package com.xjhqre.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.constant.ConfigConstant;
import com.xjhqre.common.domain.admin.Config;
import com.xjhqre.common.text.Convert;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.portal.mapper.ConfigMapper;
import com.xjhqre.portal.service.ConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * 参数配置 服务层实现
 * 
 * @author xjhqre
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 分页查询配置
     * 
     * @param config
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Config> findConfig(Config config, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(config.getConfigId() != null, Config::getConfigId, config.getConfigId())
            .eq(config.getConfigKey() != null, Config::getConfigKey, config.getConfigKey());
        return this.configMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey
     *            参数key
     * @return 参数键值 config_value
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(this.redisCache.getCacheObject(this.getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Config::getConfigKey, configKey);
        Config retConfig = this.configMapper.selectOne(queryWrapper);
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
        String captchaEnabled = this.selectConfigByKey(ConfigConstant.CAPTCHA_ENABLED);
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 获取文章审核开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectArticleAuditEnabled() {
        String articleAudit = this.selectConfigByKey(ConfigConstant.ARTICLE_AUDIT);
        if (StringUtils.isEmpty(articleAudit)) {
            return true;
        }
        return Convert.toBool(articleAudit);
    }

    /**
     * 获取图片审核开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectPictureAuditEnabled() {
        String pictureAudit = this.selectConfigByKey(ConfigConstant.PICTURE_AUDIT);
        if (StringUtils.isEmpty(pictureAudit)) {
            return true;
        }
        return Convert.toBool(pictureAudit);
    }

    /**
     * 获取全文检索开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectEsSearch() {
        String esSearch = this.selectConfigByKey(ConfigConstant.ES_SEARCH);
        if (StringUtils.isEmpty(esSearch)) {
            return true;
        }
        return Convert.toBool(esSearch);
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
