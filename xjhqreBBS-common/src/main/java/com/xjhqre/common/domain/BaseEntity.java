package com.xjhqre.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Entity基类
 * 
 * @author xjhqre
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @ApiModelProperty(name = "创建者", hidden = true)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "创建时间", hidden = true)
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(name = "更新者", hidden = true)
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "更新时间", hidden = true)
    private Date updateTime;

    /** 备注 */
    @ApiModelProperty(name = "备注")
    private String remark;

    /** 请求参数 */
    @TableField(exist = false)
    @ApiModelProperty(name = "请求参数", hidden = true)
    private Map<String, Object> params;
}
