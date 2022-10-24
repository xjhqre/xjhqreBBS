package com.xjhqre.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.common.R;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.portal.Collect;
import com.xjhqre.common.domain.portal.vo.CollectVO;
import com.xjhqre.portal.service.CollectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * CollectController
 * </p>
 *
 * @author xjhqre
 * @since 10月 20, 2022
 */
@Api(value = "收藏操作接口", tags = "收藏操作接口")
@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController {

    @Autowired
    CollectService collectService;

    @ApiOperation(value = "分页查询收藏文章")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findCollect/{pageNum}/{pageSize}")
    public R<IPage<CollectVO>> findCollect(Collect collect, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.collectService.findCollect(collect, pageNum, pageSize));
    }

    @ApiOperation(value = "添加收藏")
    @PostMapping(value = "/addCollect")
    public R<String> addCollect(@RequestBody @Validated Collect collect) {
        this.collectService.addCollect(collect);
        return R.success("添加收藏成功");
    }

    @ApiOperation(value = "取消收藏")
    @DeleteMapping(value = "/deleteCollect/{collectId}")
    public R<String> deleteCollect(@PathVariable Long collectId) {
        this.collectService.deleteCollect(collectId);
        return R.success("取消收藏成功");
    }
}
