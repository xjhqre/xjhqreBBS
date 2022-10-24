package com.xjhqre.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements SortService {

    @Autowired
    SortMapper sortMapper;

    /**
     * 分页查询分类列表
     * 
     * @param sort
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Sort> findSort(Sort sort, Integer pageNum, Integer pageSize) {
        return this.sortMapper.findSort(new Page<>(pageNum, pageSize), sort);
    }

    /**
     * 查询所有文章分类
     * 
     * @return
     */
    @Override
    public List<Sort> listSort(Integer limit) {
        return this.sortMapper.listSort(limit);
    }

    /**
     * 引用数量-1
     * 
     * @param sortId
     */
    @Override
    public void subRefCount(Long sortId) {
        this.sortMapper.subRefCount(sortId);
    }

    /**
     * 引用数量+1
     * 
     * @param sortId
     */
    @Override
    public void addRefCount(Long sortId) {
        this.sortMapper.addRefCount(sortId);
    }

    /**
     * 添加分类
     * 
     * @param sort
     */
    @Override
    public void addSort(Sort sort) {
        this.sortMapper.insert(sort);
    }

    /**
     * 修改分类
     * 
     * @param sort
     */
    @Override
    public void updateSort(Sort sort) {
        this.sortMapper.updateById(sort);
    }

    /**
     * 逻辑删除分类
     * 
     * @param sortId
     */
    @Override
    public void deleteSort(Long sortId) {
        Sort sort = this.sortMapper.selectById(sortId);
        sort.setDelFlag("2");
        this.sortMapper.updateById(sort);
    }
}
