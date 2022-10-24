package com.xjhqre.portal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface SortMapper extends BaseMapper<Sort> {

    /**
     * 查询所有文章分类
     * 
     * @return
     */
    List<Sort> listSort(Integer limit);

    /**
     * 引用数量-1
     * 
     * @param sortId
     */
    void subRefCount(Long sortId);

    /**
     * 引用数量+1
     * 
     * @param sortId
     */
    void addRefCount(Long sortId);

    /**
     * 分页查询分类列表
     * 
     * @param sortPage
     * @param sort
     */
    IPage<Sort> findSort(@Param("sortPage") Page<Sort> sortPage, @Param("sort") Sort sort);
}
