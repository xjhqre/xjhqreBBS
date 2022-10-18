package com.xjhqre.portal.service;

import java.util.List;

import com.xjhqre.common.domain.portal.Sort;

/**
 * <p>
 * SortService
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
public interface SortService {

    /**
     * 查询所有文章分类
     * 
     * @return
     */
    List<Sort> listAllSort();
}
