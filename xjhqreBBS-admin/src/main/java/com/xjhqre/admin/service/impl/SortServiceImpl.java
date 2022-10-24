package com.xjhqre.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.SortMapper;
import com.xjhqre.admin.service.SortService;
import com.xjhqre.common.domain.portal.Sort;
import com.xjhqre.common.exception.ServiceException;

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
     * 查询所有文章分类
     * 
     * @return
     */
    @Override
    public List<Sort> listSort(Integer limit) {
        return this.sortMapper.listSort(limit);
    }

    /**
     * 根据条件分页查询文章分类
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
     * 添加分类
     * 
     * @param sort
     */
    @Override
    public void add(Sort sort) {
        this.sortMapper.insert(sort);
    }

    /**
     * 修改分类
     * 
     * @param sort
     */
    @Override
    public void update(Sort sort) {
        this.sortMapper.updateById(sort);
    }

    @Override
    public void delete(Long sortId) {
        Sort sort = this.sortMapper.selectById(sortId);
        if (sort.getRefCount() > 0) {
            throw new ServiceException("分类被引用中，无法删除");
        }
        this.sortMapper.deleteById(sortId);
    }
}
