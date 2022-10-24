package com.xjhqre.portal.service;

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
     * 分页查询分类列表
     * 
     * @param sort
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Sort> findSort(Sort sort, Integer pageNum, Integer pageSize);

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
     * 添加分类
     * 
     * @param sort
     */
    void addSort(Sort sort);

    /**
     * 修改分类
     * 
     * @param sort
     */
    void updateSort(Sort sort);

    /**
     * 逻辑删除分类
     * 
     * @param sortId
     */
    void deleteSort(Long sortId);
}
