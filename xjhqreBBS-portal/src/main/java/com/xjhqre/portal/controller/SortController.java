package com.xjhqre.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjhqre.common.common.R;
import com.xjhqre.common.domain.portal.Sort;
import com.xjhqre.portal.service.SortService;

import io.swagger.annotations.Api;
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

    @ApiOperation(value = "查询分类列表")
    @GetMapping(value = "/list}")
    public R<List<Sort>> list() {
        // 查询所有文章分类
        return R.success(this.sortService.listAllSort());
    }
}
