package com.xjhqre.common.domain.portal.vo;

import com.xjhqre.common.domain.portal.Comment;

import lombok.Data;

/**
 * <p>
 * CommentVO
 * </p>
 *
 * @author xjhqre
 * @since 10月 14, 2022
 */
@Data
public class CommentVO extends Comment {

    // 回复对象用户名
    private String replyName;
}
