package com.xjhqre.admin.controller.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mq.RabbitMQSender;
import com.xjhqre.admin.service.ArticleService;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.ArticleStatus;
import com.xjhqre.common.constant.ErrorCode;
import com.xjhqre.common.core.BaseController;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xjhqre
 */
@Api(value = "文章管理接口", tags = "文章管理接口")
@RestController
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    RabbitMQSender rabbitMQSender;

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
                .eq(article.getStatus() != null, Article::getStatus, ArticleStatus.PUBLISH)
                .eq(article.getAuthor() != null, Article::getAuthor, article.getAuthor())
                .like(article.getCreateBy() != null, Article::getCreateBy, article.getCreateBy());
        return R.success(this.articleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @ApiOperation(value = "根据文章编号获取文章详细")
    @GetMapping(value = "/{articleId}")
    public R<Article> getArticleDetails(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException(ErrorCode.NULL_EXCEPTION, "不允许传入空值");
        }
        return R.success(this.articleService.getById(articleId));
    }

    @ApiOperation(value = "审核文章")
    @GetMapping(value = "/audit/{articleId}/{isPass}")
    @PreAuthorize("@ss.hasPermission('portal:article:audit')")
    public R<String> audit(@PathVariable Long articleId, @PathVariable String isPass) {
        Article article = this.articleService.getById(articleId);
        if ("Y".equals(isPass)) {
            article.setStatus(ArticleStatus.PUBLISH);
            this.rabbitMQSender.sendArticleSaveMessage(String.valueOf(articleId));
        } else {
            article.setStatus(ArticleStatus.GOBACK);
        }
        this.articleService.updateById(article);
        return R.success("审核完成");
    }

    @ApiOperation(value = "删除文章")
    @DeleteMapping(value = "/delete/{articleId}")
    @PreAuthorize("@ss.hasPermission('portal:article:delete')")
    public R<String> delete(@PathVariable Long articleId) {
        if (articleId == null) {
            throw new ServiceException(ErrorCode.NULL_EXCEPTION, "不允许传入空值");
        }
        this.articleService.deleteArticleById(articleId);
        return R.success("删除文章成功");
    }

}
