package com.xjhqre.portal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 查询指定数量的标签
     *
     * @param limit
     * @return
     */
    List<Tag> listTag(Integer limit);

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

    /**
     * 分页查询标签列表
     * 
     * @param tagPage
     * @param tag
     * @return
     */
    IPage<Tag> findTag(@Param("tagPage") Page<Tag> tagPage, @Param("tag") Tag tag);
}
