package com.xjhqre.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.ArticleMapper;
import com.xjhqre.admin.mq.RabbitMQSender;
import com.xjhqre.admin.service.ArticleService;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    RedisCache redisCache;
    @Autowired
    RabbitMQSender rabbitMQSender;

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

}
