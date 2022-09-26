package com.xjhqre.admin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by lhr on 17-7-31.
 */

@Data
@ApiModel("角色模型")
@TableName("t_role")
public class Role implements Serializable {

    @ApiModelProperty(value = "唯一uid", hidden = true)
    @TableId(value = "uid", type = IdType.INPUT)
    private Long uid;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

}
