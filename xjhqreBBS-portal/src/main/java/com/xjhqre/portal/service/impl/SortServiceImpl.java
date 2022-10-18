package com.xjhqre.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjhqre.common.domain.portal.Sort;
import com.xjhqre.portal.mapper.SortMapper;
import com.xjhqre.portal.service.SortService;

/**
 * <p>
 * SortServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SortServiceImpl implements SortService {

    @Autowired
    SortMapper sortMapper;

    /**
     * 查询所有文章分类
     * 
     * @return
     */
    @Override
    public List<Sort> listAllSort() {
        return this.sortMapper.listAllSort();
    }
}
