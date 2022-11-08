package com.xjhqre.common.domain.sms;

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
 * 用户消息表-赞和收藏
 * </p>
 *
 * @author xjhqre
 * @since 2022-11-07
 */

@ApiModel(value = "Message3对象", description = "用户消息表-赞和收藏")
@TableName("t_message_3")
@Data
public class Message3 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息ID", example = "0")
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty(value = "点赞收藏人id", example = "0")
    @NotNull
    private Long collectorId;

    @ApiModelProperty(value = "点赞收藏人名称")
    private String collectorName;

    @ApiModelProperty(value = "被点赞收藏人id", example = "0")
    @NotNull
    private Long collectedId;

    @ApiModelProperty(value = "被点赞收藏人名称")
    private String collectedName;

    @ApiModelProperty(value = "0点赞1收藏")
    @NotBlank
    private String messageType;

    @ApiModelProperty(value = "文章id")
    @NotNull
    private Long articleId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "删除标志（0：正常 2：删除）")
    private String delFlag;
}
