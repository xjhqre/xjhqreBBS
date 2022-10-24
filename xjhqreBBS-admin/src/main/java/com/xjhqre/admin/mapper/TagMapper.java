package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

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
     * 引用数量-1
     * 
     * @param tagId
     */
    void subRefCount(Long tagId);

    /**
     * 引用数量+1
     * 
     * @param tagId
     */
    void addRefCount(Long tagId);
}
