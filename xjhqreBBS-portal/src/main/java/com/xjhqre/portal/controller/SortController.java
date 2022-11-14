package com.xjhqre.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.common.R;
import com.xjhqre.common.core.BaseController;
import com.xjhqre.common.domain.portal.Sort;
import com.xjhqre.portal.service.SortService;

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
@RequestMapping("/portal/sort")
public class SortController extends BaseController {

    @Autowired
    SortService sortService;

    @ApiOperation(value = "分页查询分类列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findSort/{pageNum}/{pageSize}")
    public R<IPage<Sort>> findSort(Sort sort, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.sortService.findSort(sort, pageNum, pageSize));
    }

    @ApiOperation(value = "查询分类列表")
    @GetMapping(value = "/list}")
    public R<List<Sort>> list(@RequestParam Integer limit) {
        // 查询所有文章分类
        return R.success(this.sortService.listSort(limit));
    }
}
