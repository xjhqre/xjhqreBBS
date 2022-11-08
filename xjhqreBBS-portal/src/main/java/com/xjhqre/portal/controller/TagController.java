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
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.portal.Tag;
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
@RequestMapping("/portal/tag")
public class TagController extends BaseController {

    @Autowired
    TagService tagService;

    @ApiOperation(value = "分页查询标签列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("find/{pageNum}/{pageSize}")
    public R<IPage<Tag>> find(Tag tag, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.tagService.findTag(tag, pageNum, pageSize));
    }

    @ApiOperation(value = "查询标签列表")
    @GetMapping(value = "/list}")
    public R<List<Tag>> list(@RequestParam Integer limit) {
        // 查询所有文章分类
        return R.success(this.tagService.listTag(limit));
    }
}
