package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.portal.Article;

/**
 * <p>
 * ArticleMapper
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Mapper
public interface ArticleMapper {

    /**
     * 根据条件分页查询文章信息
     * 
     * @param articlePage
     * @param article
     * @return
     */
    IPage<Article> findArticle(@Param("articlePage") Page<Article> articlePage, @Param("article") Article article);

    /**
     * 根据文章id查询文章详情
     * 
     * @param articleId
     * @return
     */
    Article selectArticleById(@Param("articleId") Long articleId);

    /**
     * 发布文章
     * 
     * @param article
     */
    int addArticle(Article article);

    /**
     * 修改文章
     * 
     * @param article
     */
    void updateArticle(Article article);

}
