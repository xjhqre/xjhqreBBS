package com.xjhqre.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.constant.PictureConstant;
import com.xjhqre.common.domain.picture.Picture;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.common.utils.FileUtils;
import com.xjhqre.common.utils.ImageUtil;
import com.xjhqre.common.utils.OSSUtil;
import com.xjhqre.common.utils.OSSUtil.FileDirType;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.common.utils.uuid.IdUtils;
import com.xjhqre.portal.mapper.PictureMapper;
import com.xjhqre.portal.mq.RabbitMQSender;
import com.xjhqre.portal.service.ConfigService;
import com.xjhqre.portal.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    ConfigService configService;
    @Autowired
    RabbitMQSender rabbitMQSender;

    /**
     * 单个上传
     *
     * @param picture
     * @param mFile
     */
    @Override
    public void savePicture(Picture picture, MultipartFile mFile) {
        String extension = FileUtils.getExtension(mFile.getOriginalFilename());

        if (!ImageUtil.SUFFIXS.contains(extension)) {
            throw new ServiceException("上传图片格式不支持！");
        }

        // 获取文件id
        String pictureId = IdUtils.simpleUUID();
        picture.setPictureId(pictureId);
        if (picture.getPicName() == null) {
            picture.setPicName(mFile.getOriginalFilename());
        }

        // 上传OSS
        String pictureUrl = OSSUtil.upload(mFile, FileDirType.PICTURE, pictureId + extension);

        picture.setUrl(pictureUrl);
        picture.setUploader(SecurityUtils.getUserId());
        picture.setCreateTime(DateUtils.getNowDate());
        picture.setCreateBy(SecurityUtils.getUsername());
        picture.setStatus(PictureConstant.TO_BE_REVIEWED); // 设置为待审核状态
        this.pictureMapper.insert(picture);
        // 如果没开启图片审核
        if (!this.configService.selectPictureAuditEnabled()) {
            picture.setStatus(PictureConstant.PROCESSING);
            // 发送到消息队列
            this.rabbitMQSender.sendPictureProcessMessage(new String[]{picture.getPictureId()});
        }
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
        queryWrapper.eq(Picture::getUploader, SecurityUtils.getUserId());
        queryWrapper.eq(picture.getPicName() != null, Picture::getPicName, picture.getPicName());
        queryWrapper.eq(picture.getStatus() != null, Picture::getStatus, picture.getStatus());
        return this.pictureMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * 批量上传
     *
     * @param multipartFileList
     */
    @Override
    public void batchUpload(List<MultipartFile> multipartFileList) {
        List<Picture> pictureList = new ArrayList<>();

        for (MultipartFile mFile : multipartFileList) {
            String extension = FileUtils.getExtension(mFile.getOriginalFilename());

            if (!ImageUtil.SUFFIXS.contains(extension)) {
                throw new ServiceException("上传图片格式不支持！");
            }
            Picture picture = new Picture();
            // 获取文件id
            String pictureId = IdUtils.simpleUUID();
            picture.setPictureId(pictureId);
            picture.setPicName(mFile.getOriginalFilename());

            // 上传OSS
            String pictureUrl = OSSUtil.upload(mFile, FileDirType.PICTURE, pictureId + extension);

            picture.setUrl(pictureUrl);
            picture.setUploader(SecurityUtils.getUserId());
            picture.setCreateTime(DateUtils.getNowDate());
            picture.setCreateBy(SecurityUtils.getUsername());
            picture.setStatus(PictureConstant.TO_BE_REVIEWED); // 设置为待审核状态
            // 如果没开启图片审核
            if (!this.configService.selectPictureAuditEnabled()) {
                picture.setStatus(PictureConstant.PROCESSING);
            }
            pictureList.add(picture);
        }
        this.saveBatch(pictureList);
        // 发送到消息队列
        this.rabbitMQSender.sendPictureProcessMessage(pictureList.stream().map(Picture::getPictureId).toArray(String[]::new));
    }
}
