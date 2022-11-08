package com.xjhqre.common.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.portal.Sort;

/**
 * <p>
 * SortService
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
public interface SortService extends IService<Sort> {

    /**
     * 查询所有文章分类
     * 
     * @return
     */
    List<Sort> listSort(Integer limit);

    /**
     * 根据条件分页查询文章分类
     * 
     * @param sort
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Sort> findSort(Sort sort, Integer pageNum, Integer pageSize);

    /**
     * 删除分类
     * 
     * @param sortId
     */
    void delete(Long sortId);
}
