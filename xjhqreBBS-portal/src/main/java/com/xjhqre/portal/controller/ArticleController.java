package com.xjhqre.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.ArticleStatus;
import com.xjhqre.common.constant.ExceptionConstants;
import com.xjhqre.common.core.BaseController;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.portal.dto.ArticleDTO;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.portal.service.ArticleService;
import com.xjhqre.portal.service.ConfigService;
import com.xjhqre.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * <p>
 * 文章操作接口
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Api(value = "文章操作接口", tags = "文章操作接口")
@RestController
@Slf4j
@RequestMapping("/portal/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @DubboReference(check = false, url = "${provider.host}")
    SearchService searchService;
    @Autowired
    ConfigService configService;

    @ApiOperation(value = "分页查询文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
                    example = "20")})
    @GetMapping("findArticle/{pageNum}/{pageSize}")
    public R<IPage<Article>> findArticle(Article article, @PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(article.getArticleId() != null, Article::getArticleId, article.getArticleId())
                .eq(article.getAuthor() != null, Article::getAuthor, article.getAuthor())
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .eq(article.getTitle() != null, Article::getTitle, article.getTitle());
        return R.success(this.articleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @ApiOperation(value = "全文检索文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
                    example = "20")})
    @GetMapping("SearchArticle/{pageNum}/{pageSize}")
    public R<IPage<Article>> searchArticle(String keywords, @PathVariable("pageNum") Integer pageNum,
                                           @PathVariable("pageSize") Integer pageSize) {
        boolean esSearch = this.configService.selectEsSearch();
        if (esSearch) {
            try {
                return R.success(this.searchService.searchArticleByES(keywords, pageNum, pageSize));
            } catch (RpcException ignored) {
                log.info("dubbo调用失败，使用SQL查询");
            }
        }
        return R.success(this.articleService.findArticleByKeywords(keywords, pageNum, pageSize));
    }

    @ApiOperation(value = "根据分类id分页查询文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
                    example = "20")})
    @GetMapping("findArticleBySortId/{pageNum}/{pageSize}")
    public R<IPage<Article>> findArticleBySortId(Long sortId, @PathVariable("pageNum") Integer pageNum,
                                                 @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.articleService.findArticleBySortId(sortId, pageNum, pageSize));
    }

    @ApiOperation(value = "根据标签id分页查询文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
                    example = "20")})
    @GetMapping("findArticleByTagId/{pageNum}/{pageSize}")
    public R<IPage<Article>> findArticleByTagId(Long tagId, @PathVariable("pageNum") Integer pageNum,
                                                @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.articleService.findArticleByTagId(tagId, pageNum, pageSize));
    }

    @ApiOperation(value = "根据文章编号获取文章详细")
    @GetMapping(value = "getArticleDetails/{articleId}")
    public R<Article> getArticleDetails(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException(ExceptionConstants.NOT_NULL);
        }
        return R.success(this.articleService.getById(articleId));
    }

    @ApiOperation(value = "浏览文章")
    @GetMapping(value = "viewArticle/{articleId}")
    public R<Article> viewArticle(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException(ExceptionConstants.NOT_NULL);
        }
        return R.success(this.articleService.viewArticle(articleId));
    }

    @ApiOperation(value = "保存为草稿")
    @PostMapping(value = "/saveDraft")
    public R<String> saveDraft(@RequestBody @Validated ArticleDTO articleDTO) {
        if (SecurityUtils.getLoginUser() == null) {
            throw new ServiceException("登陆之后才能发布文章！！！");
        }
        this.articleService.saveDraft(articleDTO);
        return R.success("保存草稿成功");
    }

    @ApiOperation(value = "直接发布文章")
    @PostMapping(value = "/directPostArticle")
    public R<String> directPostArticle(@RequestBody @Validated ArticleDTO articleDTO) {
        if (SecurityUtils.getLoginUser() == null) {
            throw new ServiceException("登陆之后才能发布文章！！！");
        }
        this.articleService.directPostArticle(articleDTO);
        return R.success("发布文章成功");
    }

    @ApiOperation(value = "重新发布文章，用于草稿和退回的文章发布")
    @PostMapping(value = "/rePostArticle")
    public R<String> rePostArticle(@RequestBody @Validated ArticleDTO articleDTO) {
        if (SecurityUtils.getLoginUser() == null) {
            throw new ServiceException("登陆之后才能发布文章！！！");
        }
        this.articleService.rePostArticle(articleDTO);
        return R.success("发布文章成功");
    }

    @ApiOperation(value = "修改文章")
    @PostMapping(value = "/update")
    public R<String> update(@RequestBody @Validated ArticleDTO articleDTO) {
        Long author = this.articleService.getById(articleDTO.getArticleId()).getAuthor();
        if (!SecurityUtils.getUserId().equals(author)) {
            throw new ServiceException("没有权限修改别人的文章");
        }
        this.articleService.updateArticle(articleDTO);
        return R.success("修改文章成功");
    }

    @ApiOperation(value = "删除文章")
    @GetMapping(value = "/delete/{articleId}")
    public R<String> delete(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException(ExceptionConstants.NOT_NULL);
        }
        Article article = this.articleService.viewArticle(articleId);
        Long author = article.getAuthor();
        if (!SecurityUtils.getUserId().equals(author)) {
            throw new ServiceException("没有权限删除别人的文章");
        }
        this.articleService.deleteArticleById(articleId);
        return R.success("删除文章成功");
    }

    /**
     * 点赞文章 / 取消点赞
     */
    @ApiOperation(value = "点赞文章 / 取消点赞")
    @PostMapping("/thumb/{articleId}")
    public R<String> likeArticle(@PathVariable("articleId") Long articleId) {
        if (articleId == null) {
            throw new ServiceException("文章id不能为空");
        }
        String msg = this.articleService.thumbArticle(articleId);
        return R.success(msg);
    }

    /**
     * 统计某篇文章总点赞数
     */
    @ApiOperation(value = "统计某篇文章总点赞数")
    @GetMapping("/totalThumb/{articleId}")
    public R<Integer> countArticleLike(@PathVariable("articleId") Long articleId) {
        if (articleId == null) {
            throw new ServiceException("文章id不能为空");
        }
        return R.success(this.articleService.countArticleLike(articleId));
    }

    /**
     * 获取所有文章的月份
     */
    @ApiOperation(value = "获取所有文章的月份")
    @GetMapping("/articleMouth")
    public R<Set<String>> articleMouth() {
        return R.success(this.articleService.getArticleMouth());
    }

    /**
     * 获取对应月份的所有文章
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
                    example = "20")})
    @ApiOperation(value = "获取所有文章的月份")
    @GetMapping("/findArticleByMonth/{pageNum}/{pageSize}")
    public R<IPage<Article>> findArticleByMonth(@RequestParam String month, @PathVariable("pageNum") Integer pageNum,
                                                @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.articleService.findArticleByMonth(month, pageNum, pageSize));
    }
}
