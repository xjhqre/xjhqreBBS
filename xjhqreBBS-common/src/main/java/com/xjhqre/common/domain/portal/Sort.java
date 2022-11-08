package com.xjhqre.common.domain.portal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xjhqre.common.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 博客分类表
 * </p>
 *
 * @author xjhqre
 * @since 2022-10-18
 */

@ApiModel(value = "Sort对象", description = "博客分类表")
@TableName("t_sort")
@Data
public class Sort extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类id", hidden = true)
    @TableId(value = "sort_id", type = IdType.AUTO)
    private Long sortId;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "引用统计", hidden = true)
    private Integer refCount;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", hidden = true)
    private String delFlag;

    @ApiModelProperty(value = "排序字段", required = true)
    @NotNull(message = "排序字段不能为空")
    private Integer sort;
}
