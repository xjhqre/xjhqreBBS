package com.xjhqre.portal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xjhqre.common.domain.portal.Sort;

/**
 * <p>
 * 文章分类 mapper层
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Mapper
public interface SortMapper {

    /**
     * 查询所有文章分类
     * 
     * @return
     */
    public List<Sort> listAllSort();
}
