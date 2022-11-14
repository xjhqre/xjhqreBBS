package com.xjhqre.portal.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.portal.Tag;

/**
 * <p>
 * 文章标签服务层
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
public interface TagService extends IService<Tag> {
    List<Tag> listTag(Integer limit);

    /**
     * 分页查询标签列表
     * 
     * @param tag
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Tag> findTag(Tag tag, Integer pageNum, Integer pageSize);

    /**
     * 删除标签
     * 
     * @param tag
     */
    void delete(Tag tag);
}
