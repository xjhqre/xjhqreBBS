package com.xjhqre.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.constant.ArticleStatus;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.portal.ArticleSort;
import com.xjhqre.common.domain.portal.ArticleTag;
import com.xjhqre.common.domain.portal.dto.ArticleDTO;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.common.utils.SearchUtils;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.portal.mapper.ArticleMapper;
import com.xjhqre.portal.mapper.ArticleSortMapper;
import com.xjhqre.portal.mapper.ArticleTagMapper;
import com.xjhqre.portal.mq.RabbitMQSender;
import com.xjhqre.portal.service.ArticleService;
import com.xjhqre.portal.service.ConfigService;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

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
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    ConfigService configService;
    @Resource
    RedisCache redisCache;
    @Resource
    ArticleSortMapper articleSortMapper;
    @Resource
    ArticleTagMapper articleTagMapper;
    @Resource
    RabbitMQSender rabbitMQSender;

    // 文章点赞锁，防止多线程修改redis时产生线程安全问题
    final Object thumbLock = new Object();
    // 文章浏览量锁，防止多线程修改redis时产生线程安全问题
    final Object viewLock = new Object();

    /**
     * 根据分类id分页查询文章
     *
     * @param sortId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findArticleBySortId(Long sortId, Integer pageNum, Integer pageSize) {
        return this.articleMapper.findArticleBySortId(new Page<>(pageNum, pageSize), sortId);
    }

    /**
     * 根据标签id查询文章
     *
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findArticleByTagId(Long tagId, Integer pageNum, Integer pageSize) {
        return this.articleMapper.findArticleByTagId(new Page<>(pageNum, pageSize), tagId);
    }

    /**
     * 浏览文章
     *
     * @param articleId
     * @return
     */
    @Override
    public Article viewArticle(Long articleId) {
        synchronized (this.viewLock) {
            // 文章浏览量+1
            List<Long> userIds = this.redisCache.getCacheList(CacheConstants.ARTICLE_VIEW_USER_KEY + articleId);
            if (userIds == null) {
                // 查询数据库
                userIds = this.articleMapper.getViewUserIds(articleId);
                if (userIds == null) {
                    userIds = new ArrayList<>();
                }
            }
            userIds.add(SecurityUtils.getUserId());
            this.redisCache.setCacheList(CacheConstants.ARTICLE_VIEW_USER_KEY + articleId, userIds);
        }
        return this.articleMapper.selectById(articleId);
    }

    /**
     * 使用SQL根据关键字检索文章
     *
     * @param keywords
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findArticleByKeywords(String keywords, Integer pageNum, Integer pageSize) {
        final String keyword = keywords.trim();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.like(Article::getTitle, keyword).or().like(Article::getSummary, keyword));
        queryWrapper.eq(Article::getStatus, ArticleStatus.PUBLISH);
        queryWrapper.orderByDesc(Article::getViewCount);

        IPage<Article> iPage = this.articleMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);

        iPage.getRecords().forEach(item -> {
            // 给标题、简介设置高亮
            item.setTitle(SearchUtils.getHitCode(item.getTitle(), keyword));
            item.setSummary(SearchUtils.getHitCode(item.getSummary(), keyword));
        });

        return iPage;
    }

    /**
     * 保存为草稿
     *
     * @param articleDTO
     */
    @Override
    public void saveDraft(ArticleDTO articleDTO) {
        articleDTO.setIsPublish("1");
        articleDTO.setStatus(0);
        // 添加文章
        this.articleMapper.insert(articleDTO);
        // 关联分类
        ArticleSort articleSort = new ArticleSort();
        articleSort.setArticleId(articleDTO.getArticleId());
        articleSort.setSortId(articleDTO.getSortId());
        this.articleSortMapper.insert(articleSort);
        // 关联标签
        for (Long tagId : articleDTO.getTagIds()) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleDTO.getArticleId());
            articleTag.setTagId(tagId);
            this.articleTagMapper.insert(articleTag);
        }
    }

    /**
     * 直接发布文章
     *
     * @param articleDTO
     * @return
     */
    @Override
    public void directPostArticle(ArticleDTO articleDTO) {
        // 设置属性
        articleDTO.setCreateBy(SecurityUtils.getUsername());
        articleDTO.setCreateTime(DateUtils.getNowDate());
        articleDTO.setAuthor(SecurityUtils.getUserId());
        articleDTO.setCollectCount(0);
        articleDTO.setThumbCount(0);
        articleDTO.setViewCount(0);
        articleDTO.setSort(5);
        articleDTO.setIsPublish("1");
        this.judgeArticleReview(articleDTO);
        // 添加文章
        this.articleMapper.insert(articleDTO);
        // 如果不需要审核则直接更新es文档
        if (Objects.equals(articleDTO.getStatus(), ArticleStatus.PUBLISH)) {
            this.rabbitMQSender.sendArticleSaveMessage(String.valueOf(articleDTO.getArticleId()));
        }
        // 关联分类
        ArticleSort articleSort = new ArticleSort();
        articleSort.setArticleId(articleDTO.getArticleId());
        articleSort.setSortId(articleDTO.getSortId());
        this.articleSortMapper.insert(articleSort);
        // 关联标签
        for (Long tagId : articleDTO.getTagIds()) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleDTO.getArticleId());
            articleTag.setTagId(tagId);
            this.articleTagMapper.insert(articleTag);
        }
    }

    /**
     * 重新发布文章
     *
     * @param articleDTO
     */
    @Override
    public void rePostArticle(ArticleDTO articleDTO) {
        // 更新文章属性
        articleDTO.setCreateBy(SecurityUtils.getUsername());
        articleDTO.setCreateTime(DateUtils.getNowDate());
        articleDTO.setAuthor(SecurityUtils.getUserId());
        articleDTO.setCollectCount(0);
        articleDTO.setThumbCount(0);
        articleDTO.setViewCount(0);
        articleDTO.setSort(5);
        articleDTO.setIsPublish("1");
        this.judgeArticleReview(articleDTO);
        this.articleMapper.updateById(articleDTO);

        // 如果不需要审核则直接更新es文档
        if (Objects.equals(articleDTO.getStatus(), ArticleStatus.PUBLISH)) {
            this.rabbitMQSender.sendArticleSaveMessage(String.valueOf(articleDTO.getArticleId()));
        }

        // 如果修改了分类，更新分类
        Long oldSortId = this.articleMapper.getSortIdByArticleId(articleDTO.getArticleId());
        if (!Objects.equals(oldSortId, articleDTO.getSortId())) {
            LambdaUpdateWrapper<ArticleSort> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ArticleSort::getArticleId, articleDTO.getArticleId()).set(ArticleSort::getSortId,
                    articleDTO.getSortId());
            this.articleSortMapper.update(null, wrapper);
        }
        // 如果修改了标签
        Set<Long> oldTagIds = this.articleMapper.getTagIdsByArticleId(articleDTO.getArticleId());
        if (!SetUtils.isEqualSet(oldTagIds, articleDTO.getTagIds())) {
            // 先删除关联在添加
            this.articleMapper.deleteArticleTag(articleDTO.getArticleId());
            // 关联标签
            for (Long tagId : articleDTO.getTagIds()) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleDTO.getArticleId());
                articleTag.setTagId(tagId);
                this.articleTagMapper.insert(articleTag);
            }
        }
    }

    /**
     * 修改文章
     *
     * @param articleDTO
     */
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        this.judgeArticleReview(articleDTO);
        articleDTO.setUpdateBy(SecurityUtils.getUsername());
        articleDTO.setUpdateTime(DateUtils.getNowDate());
        // 更新文章表
        this.articleMapper.updateById(articleDTO);

        // 如果不需要审核则直接更新es文档
        if (Objects.equals(articleDTO.getStatus(), ArticleStatus.PUBLISH)) {
            this.rabbitMQSender.sendArticleSaveMessage(String.valueOf(articleDTO.getArticleId()));
        }

        // 如果修改了分类
        Long oldSortId = this.articleMapper.getSortIdByArticleId(articleDTO.getArticleId());
        if (!Objects.equals(oldSortId, articleDTO.getSortId())) {
            LambdaUpdateWrapper<ArticleSort> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ArticleSort::getArticleId, articleDTO.getArticleId()).set(ArticleSort::getSortId,
                    articleDTO.getSortId());
            this.articleSortMapper.update(null, wrapper);
        }

        // 如果修改了标签
        Set<Long> oldTagIds = this.articleMapper.getTagIdsByArticleId(articleDTO.getArticleId());
        if (!SetUtils.isEqualSet(oldTagIds, articleDTO.getTagIds())) {
            // 先删除关联在添加
            this.articleMapper.deleteArticleTag(articleDTO.getArticleId());
            // 关联标签
            for (Long tagId : articleDTO.getTagIds()) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleDTO.getArticleId());
                articleTag.setTagId(tagId);
                this.articleTagMapper.insert(articleTag);
            }
        }
    }

    /**
     * 判断是否需要审核文章
     */
    public void judgeArticleReview(ArticleDTO articleDTO) {
        if (this.configService.selectArticleAuditEnabled()) {
            articleDTO.setStatus(ArticleStatus.PENDING_REVIEW); // 待审核状态
        } else {
            articleDTO.setStatus(ArticleStatus.PUBLISH); // 发布状态
        }
    }

    /**
     * 删除文章
     *
     * @param articleId
     */
    @Override
    public void deleteArticleById(Long articleId) {
        Article article = this.articleMapper.selectById(articleId);
        article.setDelFlag("2"); // 逻辑删除
        article.setUpdateBy(SecurityUtils.getUsername());
        article.setUpdateTime(DateUtils.getNowDate());
        this.articleMapper.updateById(article);
        this.rabbitMQSender.sendArticleDeleteMessage(String.valueOf(articleId));
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

        synchronized (this.thumbLock) {
            // articleIds：用户点赞过的文章id
            Set<Long> articleIds =
                    this.redisCache.getCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId());
            if (Boolean.TRUE.equals(isThumb)) {
                // 用户已点赞文章，取消点赞
                if (articleIds == null) { // 应该不会走这里
                    // 修改数据库，删除点赞的文章记录
                    this.articleMapper.deleteThumbArticle(SecurityUtils.getUserId(), articleId);
                } else {
                    // 删除 redis 中的记录
                    // 更新用户点赞的文章 HashMap<用户id, Set<文章id>>
                    articleIds.remove(articleId);
                    this.redisCache.setCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId(),
                            articleIds);
                }

                // 更新文章点赞的用户信息 HashMap<文章id, Set<用户id>>
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
                // 更新用户点赞的文章 HashMap<用户id, Set<文章id>>
                this.redisCache.setCacheSet(CacheConstants.USER_THUMB_ARTICLE_KEY + SecurityUtils.getUserId(),
                        articleIds);

                // 更新文章点赞的用户信息 HashMap<文章id, Set<用户id>>
                Set<Long> userIds = this.redisCache.getCacheSet(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId);
                userIds.add(SecurityUtils.getUserId());
                this.redisCache.setCacheSet(CacheConstants.ARTICLE_LIKED_USER_KEY + articleId, userIds);

                // 通知文章作者
                this.rabbitMQSender.sendThumbMessage(articleId);
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
            Article article = this.articleMapper.selectById(articleId);
            return article.getThumbCount();
        } else {
            return userIds.size();
        }
    }

    /**
     * 获取所有文章的月份
     *
     * @return
     */
    @Override
    public Set<String> getArticleMouth() {
        List<Article> articles = this.articleMapper.selectList(null);

        Set<String> monthSet = new TreeSet<>();
        for (Article article : articles) {
            Date createTime = article.getCreateTime();
            if (createTime != null) {
                String month = new SimpleDateFormat("yyyy年MM月").format(createTime);
                monthSet.add(month);
            }
        }
        return monthSet;
    }

    /**
     * 获取所有文章的月份
     *
     * @param month
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findArticleByMonth(String month, Integer pageNum, Integer pageSize) {
        Date curMonth = DateUtils.parseDate(month);
        Date lastMonth = DateUtils.stepMonth(curMonth, 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article.class, article -> !article.getProperty().equals("content")) // 不返回文章内容
                // 查询这个月之间的文章
                .between(Article::getCreateTime, curMonth, lastMonth);
        return this.articleMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
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
