package com.xjhqre.portal.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.ArticleStatus;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.portal.dto.ArticleDTO;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.portal.service.ArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "分页查询文章列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findArticle/{pageNum}/{pageSize}")
    public R<IPage<Article>> findArticle(Article article, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.articleService.findArticle(article, pageNum, pageSize));
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
    @GetMapping(value = "/{articleId}")
    public R<Article> getArticleDetails(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException("不允许传入空值");
        }
        return R.success(this.articleService.selectArticleById(articleId));
    }

    @ApiOperation(value = "发布文章")
    @PostMapping(value = "/uploadArticle")
    public R<String> uploadArticle(@RequestBody @Validated ArticleDTO articleDTO) {
        if (SecurityUtils.getLoginUser() == null) {
            throw new ServiceException("登陆之后才能发布文章！！！");
        }
        this.articleService.addArticle(articleDTO);
        return R.success("发布文章成功");
    }

    @ApiOperation(value = "保存为草稿")
    @PostMapping(value = "/saveDrafts")
    public R<String> saveDrafts(@RequestBody @Validated ArticleDTO articleDTO) {
        articleDTO.setStatus(ArticleStatus.DRAFT); // 草稿
        articleDTO.setAuthor(this.getUsername());
        articleDTO.setCollectCount(0);
        articleDTO.setThumbCount(0);
        articleDTO.setViewCount(0);
        articleDTO.setSort(5);
        articleDTO.setIsPublish("1");
        articleDTO.setCreateBy(this.getUsername());
        articleDTO.setCreateTime(DateUtils.getNowDate());
        this.articleService.addArticle(articleDTO);
        return R.success("保存草稿成功");
    }

    @ApiOperation(value = "修改文章")
    @PostMapping(value = "/update")
    public R<String> update(@RequestBody @Validated ArticleDTO articleDTO) {
        String author = articleDTO.getAuthor();
        if (!SecurityUtils.getUsername().equals(author)) {
            throw new ServiceException("没有权限修改别人的文章");
        }
        this.articleService.updateArticle(articleDTO);
        return R.success("修改文章成功");
    }

    @ApiOperation(value = "删除文章")
    @GetMapping(value = "/delete/{articleId}")
    public R<String> delete(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException("不允许传入空值");
        }
        Article article = this.articleService.selectArticleById(articleId);
        String author = article.getAuthor();
        if (!SecurityUtils.getUsername().equals(author)) {
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
