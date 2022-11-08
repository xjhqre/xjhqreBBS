package com.xjhqre.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjhqre.common.domain.portal.Tag;

/**
 * <p>
 * 文章分类 mapper层
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 统计标签引用
     *
     * @param tagId
     * @return
     */
    Integer countRef(@Param("tagId") Long tagId);
}
