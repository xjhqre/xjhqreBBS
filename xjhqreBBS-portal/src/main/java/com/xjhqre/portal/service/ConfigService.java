package com.xjhqre.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.admin.Config;

/**
 * 参数配置 服务层
 * 
 * @author xjhqre
 */
public interface ConfigService extends IService<Config> {

    /**
     * 分页查询配置信息
     * 
     * @param config
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Config> findConfig(Config config, Integer pageNum, Integer pageSize);

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey
     *            参数键名
     * @return 参数键值
     */
    String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    boolean selectCaptchaEnabled();

    /**
     * 获取文章审核开关
     *
     * @return true开启，false关闭
     */
    boolean selectArticleAuditEnabled();

    /**
     * 获取图片审核开关
     *
     * @return true开启，false关闭
     */
    boolean selectPictureAuditEnabled();

    /**
     * 获取全文检索开关
     *
     * @return true开启，false关闭
     */
    boolean selectEsSearch();
}
