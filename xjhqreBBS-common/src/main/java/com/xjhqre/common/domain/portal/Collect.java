package com.xjhqre.common.domain.portal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xjhqre.common.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author xjhqre
 * @since 2022-10-20
 */

@ApiModel(value = "Collect对象", description = "收藏表")
@TableName("t_collect")
@Data
public class Collect extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收藏id", example = "0")
    @TableId(value = "collect_id", type = IdType.AUTO)
    private Long collectId;

    @ApiModelProperty(value = "用户的uid", example = "0")
    private Long userId;

    @ApiModelProperty(value = "博客的uid", example = "0")
    @NotNull(message = "博客id不能为空")
    private Long articleId;

    @NotBlank(message = "博客标题不能为空")
    @Size(max = 50, message = "博客标题长度不能超过50个字符")
    @ApiModelProperty(value = "博客标题")
    private String articleTitle;

    @ApiModelProperty(value = "状态", example = "0")
    private Integer status;

}
