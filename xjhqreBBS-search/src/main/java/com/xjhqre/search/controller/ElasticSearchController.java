package com.xjhqre.search.controller;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.common.R;
import com.xjhqre.common.core.BaseController;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.search.service.ArticleService;
import com.xjhqre.search.service.SearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * PictureController
 * </p>
 *
 * @author xjhqre
 * @since 10月 25, 2022
 */
@Api(value = "es搜索接口", tags = "es搜索接口")
@RestController
@RequestMapping("/search")
public class ElasticSearchController extends BaseController {

    @Autowired
    SearchService searchService;
    @Autowired
    ArticleService articleService;

    @ApiOperation(value = "通过ElasticSearch搜索博客")
    @GetMapping("/elasticSearchArticle")
    public R<IPage<Article>> searchArticle(@RequestParam(required = false) String keywords,
        @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

        if (StringUtils.isEmpty(keywords)) {
            return R.success(this.articleService.page(new Page<>(pageNum, pageSize)));
        }
        keywords = QueryParser.escape(keywords);
        IPage<Article> articleIPage = this.searchService.searchArticleByES(keywords, pageNum, pageSize);
        return R.success(articleIPage);
    }
}
