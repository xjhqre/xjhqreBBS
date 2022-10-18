package com.xjhqre.portal.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.constant.ArticleStatus;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.portal.dto.ArticleDTO;
import com.xjhqre.common.service.ConfigService;
import com.xjhqre.common.utils.DateUtils;
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
    private ConfigService configService;
    @Autowired
    RedisCache redisCache;

    // 文章点赞锁，防止多线程修改redis时产生线程安全问题
    final Object thumbLock = new Object();
    // 文章浏览量锁，防止多线程修改redis时产生线程安全问题
    final Object viewLock = new Object();

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
        synchronized (this.viewLock) {
            // 文章浏览量+1
            List<Long> userIds = this.redisCache.getCacheList(CacheConstants.ARTICLE_VIEW_USER_KEY + articleId);
            if (userIds == null) {
                userIds = new ArrayList<>();
            }
            userIds.add(SecurityUtils.getUserId());
            this.redisCache.setCacheList(CacheConstants.ARTICLE_VIEW_USER_KEY + articleId, userIds);
        }
        return this.articleMapper.selectArticleById(articleId);
    }

    /**
     * 发布新文章
     * 
     * @param articleDTO
     * @return
     */
    @Override
    public void addArticle(ArticleDTO articleDTO) {
        // 是否开启文章审核
        if (this.configService.selectArticleAuditEnabled()) {
            articleDTO.setStatus(ArticleStatus.PENDING_REVIEW); // 待审核状态
        } else {
            articleDTO.setStatus(ArticleStatus.PUBLISH); // 发布状态
        }
        articleDTO.setAuthor(SecurityUtils.getUsername());
        articleDTO.setCollectCount(0);
        articleDTO.setThumbCount(0);
        articleDTO.setViewCount(0);
        articleDTO.setSort(5);
        articleDTO.setIsPublish("1");
        articleDTO.setCreateBy(SecurityUtils.getUsername());
        articleDTO.setCreateTime(DateUtils.getNowDate());
        // 添加文章
        this.articleMapper.addArticle(articleDTO);
        // 关联分类
        this.articleMapper.addArticleSort(articleDTO.getArticleId(), articleDTO.getSortId());
        // 关联标签
        this.articleMapper.addArticleTag(articleDTO.getArticleId(), articleDTO.getTagIds());
    }

    /**
     * 修改文章
     * 
     * @param articleDTO
     */
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        articleDTO.setUpdateBy(SecurityUtils.getUsername());
        articleDTO.setUpdateTime(DateUtils.getNowDate());
        // 先删除关联在添加
        this.articleMapper.deleteArticleSort(articleDTO.getArticleId());
        this.articleMapper.deleteArticleTag(articleDTO.getArticleId());
        // 关联分类
        this.articleMapper.addArticleSort(articleDTO.getArticleId(), articleDTO.getSortId());
        // 关联标签
        this.articleMapper.addArticleTag(articleDTO.getArticleId(), articleDTO.getTagIds());
        // 更新文章表
        this.articleMapper.updateArticle(articleDTO);
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
        article.setUpdateBy(SecurityUtils.getUsername());
        article.setUpdateTime(DateUtils.getNowDate());
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

        synchronized (this.viewLock) {
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
