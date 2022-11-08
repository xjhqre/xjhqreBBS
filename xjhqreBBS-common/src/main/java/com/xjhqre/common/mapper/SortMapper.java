package com.xjhqre.common.mapper;

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
     * 根据条件分页查询文章分类
     * 
     * @param sortPage
     * @param sort
     * @return
     */
    IPage<Sort> findSort(@Param("sortPage") Page<Sort> sortPage, @Param("sort") Sort sort);

    /**
     * 统计引用次数
     *
     * @param sortId
     * @return
     */
    Integer countRef(@Param("sortId") Long sortId);

}
