package com.xjhqre.search.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.search.ESArticleIndex;

/**
 * <p>
 * SearchService
 * </p>
 *
 * @author xjhqre
 * @since 11月 10, 2022
 */
public interface SearchService extends IService<ESArticleIndex> {
    /**
     * 全文检索文章
     * 
     * @param keywords
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Article> searchArticleByES(String keywords, Integer pageNum, Integer pageSize);
}
