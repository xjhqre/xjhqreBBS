package com.xjhqre.admin.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.admin.mapper.PictureMapper;
import com.xjhqre.admin.service.PictureService;
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

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * ArticleServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

    @Autowired
    PictureMapper pictureMapper;

    /**
     * 上传图片，管理员上传无需审核
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

        // 生成文件id
        String pictureId = IdUtils.simpleUUID();
        picture.setPictureId(pictureId);
        if (picture.getPicName() == null) {
            picture.setPicName(mFile.getOriginalFilename());
        }

        // 上传OSS
        String pictureUrl = OSSUtil.upload(mFile, FileDirType.PICTURE, pictureId + extension);

        picture.setUrl(pictureUrl);
        picture.setCreateTime(DateUtils.getNowDate());
        picture.setCreateBy(SecurityUtils.getUsername());
        picture.setStatus(PictureConstant.PASS); // 设置为发布状态，管理员上传的图片不用审核

        // 存入数据库
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
     * 审核图片
     * 
     * @param pictureId
     * @param result
     *            审核结果 0：不通过 1：通过
     */
    @Override
    public void audit(String pictureId, Integer result) {
        Picture picture = this.pictureMapper.selectById(pictureId);
        if (result == 1) {
            // 传输图片的本地地址给 Python 程序，返回 OSS url地址
            // TODO 异步执行
            picture.setStatus(PictureConstant.PROCESSING);
            this.pictureMapper.updateById(picture); // 处理过程较长，大约几秒，先保存状态
            String response = this.executePython(pictureId, picture.getUrl());
            if (response.contains("200")) {
                // 审核通过
                picture.setStatus(PictureConstant.PASS);
            } else {
                // 审核出现异常，回到待审核状态
                picture.setStatus(PictureConstant.TO_BE_REVIEWED);
            }
        } else {
            picture.setStatus(PictureConstant.FAILED);
        }
        this.pictureMapper.updateById(picture);
    }

    /**
     * 调用 Python 程序，上传本地到 OSS，解析图片向量保存到es
     * 
     * @param pictureId
     *            图片id
     * 
     * @param pictureUrl
     *            图片OSS地址
     */
    public String executePython(String pictureId, String pictureUrl) {
        StringBuilder response = new StringBuilder();
        try {
            // 参数1：解释器地址 参数二：Python程序地址 参数三：图片id 参数四：图片OSS地址。调用Python上传图片特征
            String[] args = new String[] {PictureConstant.INTERPRETER, PictureConstant.OFFLINE, pictureId, pictureUrl};
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append('\n');
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}
