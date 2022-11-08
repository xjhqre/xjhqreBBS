package com.xjhqre.admin.controller.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.portal.Tag;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.common.service.TagService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 文章标签操作接口
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Api(value = "文章标签操作接口", tags = "文章标签操作接口")
@RestController
@RequestMapping("/articleTag")
public class TagController extends BaseController {

    @Autowired
    TagService tagService;

    @ApiOperation(value = "查询标签列表")
    @GetMapping(value = "/list}")
    @PreAuthorize("@ss.hasPermission('article:tag:list')")
    public R<List<Tag>> list(@RequestParam Integer limit) {
        // 查询所有文章分类
        return R.success(this.tagService.listTag(limit));
    }

    @ApiOperation(value = "分页查询标签分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @PreAuthorize("@ss.hasPermission('article:tag:list')")
    @GetMapping("findTag/{pageNum}/{pageSize}")
    public R<IPage<Tag>> findTag(Tag tag, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.tagService.findTag(tag, pageNum, pageSize));
    }

    @ApiOperation(value = "添加标签")
    @PostMapping(value = "/add}")
    @PreAuthorize("@ss.hasPermission('article:tag:insert')")
    @Log(title = "标签管理", businessType = BusinessType.INSERT)
    public R<String> add(@RequestBody @Validated Tag tag) {
        this.tagService.save(tag);
        return R.success("添加标签成功");
    }

    @ApiOperation(value = "修改标签")
    @PutMapping(value = "/update}")
    @PreAuthorize("@ss.hasPermission('article:tag:update')")
    @Log(title = "标签管理", businessType = BusinessType.UPDATE)
    public R<String> update(@RequestBody @Validated Tag tag) {
        this.tagService.updateById(tag);
        return R.success("修改标签成功");
    }

    @ApiOperation(value = "删除标签")
    @PutMapping(value = "/delete}")
    @PreAuthorize("@ss.hasPermission('article:tag:delete')")
    @Log(title = "标签管理", businessType = BusinessType.DELETE)
    public R<String> delete(@RequestBody @Validated Tag tag) {
        this.tagService.delete(tag);
        return R.success("删除标签成功");
    }
}
