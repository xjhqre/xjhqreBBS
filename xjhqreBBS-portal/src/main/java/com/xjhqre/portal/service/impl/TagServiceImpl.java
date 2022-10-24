package com.xjhqre.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.Tag;
import com.xjhqre.portal.mapper.TagMapper;
import com.xjhqre.portal.service.TagService;

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
        return this.tagMapper.listTag(limit);
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
     * 分页查询标签列表
     * 
     * @param tag
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Tag> findTag(Tag tag, Integer pageNum, Integer pageSize) {
        return this.tagMapper.findTag(new Page<Tag>(pageNum, pageSize), tag);

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
     * @param tagId
     */
    @Override
    public void deleteTag(Long tagId) {
        Tag tag = this.tagMapper.selectById(tagId);
        tag.setDelFlag("2");
        this.tagMapper.updateById(tag);
    }
}
