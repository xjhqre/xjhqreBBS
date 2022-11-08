package com.xjhqre.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.Tag;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.mapper.TagMapper;
import com.xjhqre.common.service.TagService;

/**
 * <p>
 * 文章标签服务实现层
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    TagMapper tagMapper;

    /**
     * 查询指定数量的标签
     * 
     * @param limit
     * @return
     */
    @Override
    public List<Tag> listTag(Integer limit) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tag::getSort); // 根据sort字段排序
        queryWrapper.last("limit " + limit); // 限制查询个数
        return this.tagMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询标签列表
     *
     * @param tag
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Tag> findTag(Tag tag, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(tag.getTagId() != null, Tag::getTagId, tag.getTagId())
            .eq(tag.getName() != null, Tag::getName, tag.getName()).orderByDesc(Tag::getSort);
        Page<Tag> tagPage = this.tagMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        // 统计标签引用
        for (Tag item : tagPage.getRecords()) {
            int count = this.tagMapper.countRef(item.getTagId());
            item.setRefCount(count);
        }
        return tagPage;
    }

    /**
     * 删除标签
     * 
     * @param tag
     */
    @Override
    public void delete(Tag tag) {
        Integer refCount = tag.getRefCount();
        if (refCount == 0) {
            refCount = this.tagMapper.countRef(tag.getTagId());
        }
        // 引用计数校验
        if (refCount > 0) {
            throw new ServiceException("标签被引用，无法删除");
        }
        this.tagMapper.deleteById(tag);
    }
}
