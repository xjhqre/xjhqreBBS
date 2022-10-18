package com.xjhqre.quartz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.quartz.mapper.ArticleQuartzMapper;
import com.xjhqre.quartz.service.ArticleQuartzService;

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
public class ArticleQuartzServiceImpl implements ArticleQuartzService {

    @Autowired
    ArticleQuartzMapper articleQuartzMapper;

    /**
     * 根据文章id查询文章详情
     *
     * @param articleId
     * @return
     */
    @Override
    public Article selectArticleById(Long articleId) {
        return this.articleQuartzMapper.selectArticleById(articleId);
    }

    /**
     * 修改文章
     * 
     * @param article
     */
    @Override
    public void updateArticle(Article article) {
        this.articleQuartzMapper.updateArticle(article);
    }
}
