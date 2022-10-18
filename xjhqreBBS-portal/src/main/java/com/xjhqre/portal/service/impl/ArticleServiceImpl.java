package com.xjhqre.portal.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.portal.mapper.ArticleMapper;
import com.xjhqre.portal.service.ArticleService;

/**
 * <p>
 * ArticleServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    RedisCache redisCache;

    // redis锁，防止多线程修改redis时产生线程安全问题
    final Object redisLock = new Object();

    /**
     * 根据条件分页查询文章列表
     * 
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findArticle(Article article, Integer pageNum, Integer pageSize) {
        article.setStatus(2); // 查询已发布的文章
        return this.articleMapper.findArticle(new Page<>(pageNum, pageSize), article);
    }

    /**
     * 根据文章id查询文章详情
     * 
     * @param articleId
     * @return
     */
    @Override
    public Article selectArticleById(Long articleId) {
        return this.articleMapper.selectArticleById(articleId);
    }

    /**
     * 发布新文章
     * 
     * @param article
     * @return
     */
    @Override
    public int addArticle(Article article) {
        return this.articleMapper.addArticle(article);
    }

    /**
     * 修改文章
     * 
     * @param article
     */
    @Override
    public void updateArticle(Article article) {
        this.articleMapper.updateArticle(article);
    }

    /**
     * 删除文章
     * 
     * @param articleId
     */
    @Override
    public void deleteArticleById(Long articleId) {
        Article article = this.selectArticleById(articleId);
        article.setDelFlag("2"); // 逻辑删除
        this.articleMapper.updateArticle(article);
    }

    /**
     * 点赞文章
     * 
     * @param articleId
     */
    @Override
    public String thumbArticle(Long articleId) {
        // 只有未点赞的用户才可以进行点赞
        Boolean isThumb = this.likeArticleLogicValidate(articleId);

        synchronized (this.redisLock) {
            Set<Long> articleIds =
                this.redisCache.getCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId());
            if (isThumb) {
                // 用户已点赞文章，取消点赞
                if (articleIds == null) { // 应该不会走这里
                    // 修改数据库，删除点赞的文章记录
                    this.articleMapper.deleteThumbArticle(SecurityUtils.getUserId(), articleId);
                } else {
                    // 删除 redis 中的记录
                    articleIds.remove(articleId);
                    this.redisCache.setCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId(),
                        articleIds);
                }
                // 文章点赞用户 - 1
                Set<Long> userIds = this.redisCache.getCacheSet(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId);
                userIds.remove(SecurityUtils.getUserId());
                this.redisCache.setCacheSet(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId, userIds);
                return "取消点赞";
            } else {
                // 用户未点赞，添加点赞数据
                if (articleIds == null) {
                    articleIds = new HashSet<>();
                }
                articleIds.add(articleId);
                this.redisCache.setCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId(),
                    articleIds);
                // 文章点赞用户 + 1
                Set<Long> userIds = this.redisCache.getCacheSet(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId);
                userIds.add(SecurityUtils.getUserId());
                this.redisCache.setCacheSet(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId, userIds);
                return "点赞成功";
            }
        }
    }

    /**
     * 统计某篇文章总点赞数
     * 
     * @param articleId
     * @return
     */
    @Override
    public Integer countArticleLike(Long articleId) {
        // 先查询redis，没有在查询数据库
        Set<Long> userIds = this.redisCache.getCacheObject(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId);
        if (userIds == null) {
            Article article = this.articleMapper.selectArticleById(articleId);
            return article.getThumbCount();
        } else {
            return userIds.size();
        }
    }

    /**
     * 点赞文章逻辑校验
     *
     * @throws
     */
    private Boolean likeArticleLogicValidate(Long articleId) {
        // 获取用户点赞的文章ID集合
        Set<Long> articleIdSet =
            this.redisCache.getCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId());
        if (articleIdSet == null) {
            // redis缓存中没有，到数据库中查询
            articleIdSet = this.articleMapper.selectUserLikedArticle(SecurityUtils.getUserId());
            return articleIdSet != null && articleIdSet.contains(articleId);
        } else {
            // 查看缓存中是否包含该文章id
            return articleIdSet.contains(articleId);
        }
    }
}
