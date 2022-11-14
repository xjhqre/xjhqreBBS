package com.xjhqre.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
     * 查询所有文章分类
     * 
     * @return
     */
    @Override
    public List<Sort> listSort(Integer limit) {
        LambdaQueryWrapper<Sort> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Sort::getSort).last("limit " + limit);
        return this.sortMapper.selectList(queryWrapper);
    }

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
        LambdaQueryWrapper<Sort> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(sort.getSortId() != null, Sort::getSortId, sort.getSortId())
            .like(sort.getName() != null, Sort::getName, sort.getName()).orderByDesc(Sort::getSort);
        Page<Sort> sortPage = this.sortMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        // 统计分类引用数量
        for (Sort item : sortPage.getRecords()) {
            int sortCount = this.sortMapper.countRef(item.getSortId());
            item.setRefCount(sortCount);
        }
        return sortPage;
    }
}
