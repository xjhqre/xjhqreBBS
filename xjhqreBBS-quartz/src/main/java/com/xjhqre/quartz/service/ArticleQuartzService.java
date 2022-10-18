package com.xjhqre.quartz.service;

import com.xjhqre.common.domain.portal.Article;

/**
 * 文章 业务层
 *
 * @author xjhqre
 */
public interface ArticleQuartzService {

    /**
     * 根据文章id查询文章详情
     *
     * @param articleId
     * @return
     */
    Article selectArticleById(Long articleId);

    /**
     * 修改文章
     *
     * @param article
     */
    void updateArticle(Article article);

}
