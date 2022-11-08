package com.xjhqre.sms.service;

import com.xjhqre.common.domain.portal.Article;

/**
 * 文章 业务层
 *
 * @author xjhqre
 */
public interface ArticleService {

    /**
     * 根据文章id查询文章详情
     *
     * @param articleId
     * @return
     */
    Article selectArticleById(Long articleId);

}
