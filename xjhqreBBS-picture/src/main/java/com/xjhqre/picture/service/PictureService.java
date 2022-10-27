package com.xjhqre.picture.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.picture.Picture;

/**
 * 图片 业务层
 *
 * @author xjhqre
 */
public interface PictureService extends IService<Picture> {

    /**
     * 保存图片
     * 
     * @param picture
     */
    void savePicture(Picture picture);

    /**
     * 分页查询图片列表
     * 
     * @param picture
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Picture> findPicture(Picture picture, Integer pageNum, Integer pageSize);
}
