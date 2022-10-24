package com.xjhqre.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.portal.Collect;
import com.xjhqre.common.domain.portal.vo.CollectVO;

/**
 * <p>
 * CollectService
 * </p>
 *
 * @author xjhqre
 * @since 10月 20, 2022
 */
public interface CollectService extends IService<Collect> {
    /**
     * 分页查询用户收藏
     * 
     * @param collect
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<CollectVO> findCollect(Collect collect, Integer pageNum, Integer pageSize);

    /**
     * 添加收藏
     * 
     * @param collect
     */
    void addCollect(Collect collect);

    /**
     * 取消收藏
     * 
     * @param collectId
     */
    void deleteCollect(Long collectId);
}
