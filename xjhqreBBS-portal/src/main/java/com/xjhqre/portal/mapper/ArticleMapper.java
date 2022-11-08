package com.xjhqre.portal.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据分类id分页查询文章
     * 
     * @param objectPage
     * @param sortId
     * @return
     */
    IPage<Article> findArticleBySortId(@Param("articlePage") Page<Article> objectPage, @Param("sortId") Long sortId);

    /**
     * 根据标签id分页查询文章
     * 
     * @param objectPage
     * @param tagId
     * @return
     */
    IPage<Article> findArticleByTagId(@Param("objectPage") Page<Article> objectPage, @Param("tagId") Long tagId);

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
    void addArticleTag(Long articleId, Set<Long> tagIds);

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

    /**
     * 根据文章id获取关联的分类id
     * 
     * @param articleId
     * @return
     */
    Long getSortIdByArticleId(Long articleId);

    /**
     * 根据文章id查询关联的标签id集合
     * 
     * @param articleId
     * @return
     */
    Set<Long> getTagIdsByArticleId(Long articleId);

    /**
     * 查询文章浏览的用户id
     * 
     * @param articleId
     * @return
     */
    List<Long> getViewUserIds(Long articleId);

    /**
     * 修改分类
     * 
     * @param articleId
     * @param sortId
     */
    void updateArticleSort(@Param("articleId") Long articleId, @Param("sortId") Long sortId);

}
