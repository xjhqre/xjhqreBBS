package com.xjhqre.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.sms.mapper.ArticleMapper;
import com.xjhqre.sms.service.ArticleService;

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

    /**
     * 根据文章id查询文章详情
     * 
     * @param articleId
     * @return
     */
    @Override
    public Article selectArticleById(Long articleId) {
        return this.articleMapper.selectArticleById(articleId);
    }

}
