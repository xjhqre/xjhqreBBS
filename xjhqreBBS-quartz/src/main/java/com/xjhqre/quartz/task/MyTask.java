package com.xjhqre.quartz.task;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.quartz.service.ArticleQuartzService;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("myTask")
@Slf4j
public class MyTask {

    @Autowired
    RedisCache redisCache;
    @Autowired
    ArticleQuartzService articleQuartzService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        log.info(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        log.info("执行有参方法：" + params);
    }

    public void ryNoParams() {
        log.info("执行无参方法");
    }

    /**
     * 保存文章点赞到数据库
     */
    public void saveArticleThumbData() {
        // 查询缓存中素所有文章的点赞信息
        Collection<String> keys = this.redisCache.keys(CacheConstants.ARTICLE_LIKED_USER_KEY + "*");
        for (String key : keys) {
            Set<Long> userIds = this.redisCache.getCacheSet(key); // 查询点赞该文章的所有用户id
            Long articleId = Long.valueOf(key.substring(CacheConstants.ARTICLE_LIKED_USER_KEY.length()));
            Article article = this.articleQuartzService.selectArticleById(articleId);
            article.setThumbCount(userIds.size());
            this.articleQuartzService.updateArticle(article);
        }
    }

    /**
     * 保存文章浏览量到数据库
     */
    public void saveArticleViewData() {
        // 查询缓存中素所有文章的点赞信息
        Collection<String> keys = this.redisCache.keys(CacheConstants.ARTICLE_VIEW_USER_KEY + "*");
        for (String key : keys) {
            List<Long> userIds = this.redisCache.getCacheList(key); // 查询点赞该文章的所有用户id
            Long articleId = Long.valueOf(key.substring(CacheConstants.ARTICLE_LIKED_USER_KEY.length()));
            Article article = this.articleQuartzService.selectArticleById(articleId);
            article.setViewCount(userIds.size());
            this.articleQuartzService.updateArticle(article);
        }
    }
}
