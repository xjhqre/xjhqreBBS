package com.xjhqre.portal.mapper;

import java.util.Set;

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

    /**
     * 查询用户点赞的文章id
     * 
     * @param userId
     * @return
     */
    Set<Long> selectUserLikedArticle(Long userId);

    /**
     * 修改数据库，删除点赞的文章记录
     * 
     * @param userId
     * @param articleId
     */
    void deleteThumbArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 添加文章分类关联
     * 
     * @param articleId
     * @param sortId
     */
    void addArticleSort(Long articleId, Long sortId);

    /**
     * 添加文章标签关联
     * 
     * @param articleId
     * @param tagIds
     */
    void addArticleTag(Long articleId, Long[] tagIds);

    /**
     * 删除文章分类关联
     * 
     * @param articleId
     */
    void deleteArticleSort(Long articleId);

    /**
     * 删除文章标签关联
     * 
     * @param articleId
     */
    void deleteArticleTag(Long articleId);
}
