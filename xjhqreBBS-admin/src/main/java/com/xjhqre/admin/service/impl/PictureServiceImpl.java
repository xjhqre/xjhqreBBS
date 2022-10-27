package com.xjhqre.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.PictureMapper;
import com.xjhqre.admin.service.PictureService;
import com.xjhqre.common.domain.picture.Picture;

/**
 * <p>
 * ArticleServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

    @Autowired
    PictureMapper pictureMapper;

    @Override
    public void savePicture(Picture picture) {
        this.pictureMapper.insert(picture);
    }

    /**
     * 分页查询图片列表
     * 
     * @param picture
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Picture> findPicture(Picture picture, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Picture> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(picture.getUploader() != null, Picture::getUploader, picture.getUploader());
        queryWrapper.eq(picture.getPicName() != null, Picture::getPicName, picture.getPicName());
        queryWrapper.eq(picture.getStatus() != null, Picture::getStatus, picture.getStatus());
        return this.pictureMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * 根据id返回图片对象
     * 
     * @param pictureId
     * @return
     */
    @Override
    public Picture selectById(Long pictureId) {
        return this.pictureMapper.selectById(pictureId);
    }
}
