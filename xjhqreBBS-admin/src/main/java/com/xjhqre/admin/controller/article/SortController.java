package com.xjhqre.admin.controller.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.admin.service.SortService;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.domain.portal.Sort;
import com.xjhqre.common.enums.BusinessType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 文章分类操作接口
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Api(value = "文章分类操作接口", tags = "文章分类操作接口")
@RestController
@RequestMapping("/articleSort")
public class SortController {

    @Autowired
    SortService sortService;

    @ApiOperation(value = "分页查询文章分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @PreAuthorize("@ss.hasPermission('article:sort:list')")
    @GetMapping("findSort/{pageNum}/{pageSize}")
    public R<IPage<Sort>> findSort(Sort sort, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.sortService.findSort(sort, pageNum, pageSize));
    }

    @ApiOperation(value = "查询分类列表")
    @GetMapping(value = "/list}")
    @PreAuthorize("@ss.hasPermission('article:sort:list')")
    public R<List<Sort>> list(@RequestParam Integer limit) {
        // 查询所有文章分类
        return R.success(this.sortService.listSort(limit));
    }

    @ApiOperation(value = "添加分类")
    @PostMapping(value = "/add}")
    @PreAuthorize("@ss.hasPermission('article:sort:insert')")
    @Log(title = "分类管理", businessType = BusinessType.INSERT)
    public R<String> addSort(@RequestBody @Validated Sort sort) {
        this.sortService.add(sort);
        return R.success("添加分类成功");
    }

    @ApiOperation(value = "修改分类")
    @PutMapping(value = "/update}")
    @PreAuthorize("@ss.hasPermission('article:sort:update')")
    @Log(title = "分类管理", businessType = BusinessType.UPDATE)
    public R<String> update(@RequestBody @Validated Sort sort) {
        this.sortService.update(sort);
        return R.success("修改分类成功");
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping(value = "/delete/{sortId}}")
    @PreAuthorize("@ss.hasPermission('article:sort:delete')")
    @Log(title = "分类管理", businessType = BusinessType.DELETE)
    public R<String> delete(@PathVariable Long sortId) {
        this.sortService.delete(sortId);
        return R.success("删除分类成功");
    }
}
