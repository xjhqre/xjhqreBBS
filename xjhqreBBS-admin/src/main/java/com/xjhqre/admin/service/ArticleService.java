package com.xjhqre.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.domain.portal.Article;

/**
 * 文章 业务层
 *
 * @author xjhqre
 */
public interface ArticleService {

    /**
     * 根据条件分页查询文章列表
     *
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Article> findArticle(Article article, Integer pageNum, Integer pageSize);

    /**
     * 根据文章id查询文章详情
     *
     * @param articleId
     * @return
     */
    Article selectArticleById(Long articleId);

    /**
     * 添加文章
     *
     * @param article
     * @return
     */
    int addArticle(Article article);

    /**
     * 修改文章
     *
     * @param article
     */
    void updateArticle(Article article);

    /**
     * 根据文章id删除文章
     *
     * @param articleId
     */
    void deleteArticleById(Long articleId);

}
