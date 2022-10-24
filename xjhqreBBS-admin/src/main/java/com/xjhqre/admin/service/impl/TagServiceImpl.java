package com.xjhqre.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.TagMapper;
import com.xjhqre.admin.service.TagService;
import com.xjhqre.common.domain.portal.Tag;
import com.xjhqre.common.exception.ServiceException;

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
            .eq(tag.getName() != null, Tag::getName, tag.getName())
            .eq(tag.getSort() != null, Tag::getSort, tag.getSort());
        return this.tagMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * 减少引用数量
     * 
     * @param tagId
     */
    @Override
    public void subRefCount(Long tagId) {
        this.tagMapper.subRefCount(tagId);
    }

    /**
     * 引用数量+1
     * 
     * @param tagId
     */
    @Override
    public void addRefCount(Long tagId) {
        this.tagMapper.addRefCount(tagId);
    }

    /**
     * 添加标签
     * 
     * @param tag
     */
    @Override
    public void addTag(Tag tag) {
        this.tagMapper.insert(tag);
    }

    /**
     * 修改标签
     * 
     * @param tag
     */
    @Override
    public void updateTag(Tag tag) {
        this.tagMapper.updateById(tag);
    }

    /**
     * 删除标签
     * 
     * @param tag
     */
    @Override
    public void deleteTag(Tag tag) {
        // 引用计数校验
        if (tag.getRefCount() > 0) {
            throw new ServiceException("标签被引用，无法删除");
        }
        this.tagMapper.deleteById(tag);
    }
}
