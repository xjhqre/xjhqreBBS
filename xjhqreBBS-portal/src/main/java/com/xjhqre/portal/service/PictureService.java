package com.xjhqre.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.picture.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * @param mFile
     */
    void savePicture(Picture picture, MultipartFile mFile);

    /**
     * 分页查询图片列表
     *
     * @param picture
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Picture> findPicture(Picture picture, Integer pageNum, Integer pageSize);

    /**
     * 批量上传图片
     *
     * @param multipartFileList
     */
    void batchUpload(List<MultipartFile> multipartFileList);
}
