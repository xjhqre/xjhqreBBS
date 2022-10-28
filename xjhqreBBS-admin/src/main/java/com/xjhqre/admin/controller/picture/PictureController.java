package com.xjhqre.admin.controller.picture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.admin.service.PictureService;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.PictureConstant;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.picture.Picture;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.common.utils.OSSUtil;
import com.xjhqre.common.utils.OSSUtil.FileDirType;
import com.xjhqre.common.utils.uuid.IdUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * PictureController
 * </p>
 *
 * @author xjhqre
 * @since 10月 25, 2022
 */
@Api(value = "图片操作接口", tags = "图片操作接口")
@RestController
@RequestMapping("/admin/picture")
public class PictureController extends BaseController {

    @Autowired
    PictureService pictureService;

    @ApiOperation(value = "分页查询图片列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("find/{pageNum}/{pageSize}")
    public R<IPage<Picture>> find(Picture picture, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.pictureService.findPicture(picture, pageNum, pageSize));
    }

    @ApiOperation(value = "上传图片")
    @PostMapping(value = "/add")
    public R<String> add(@Validated Picture picture, @RequestParam("file") MultipartFile mFile) {
        if (mFile == null) {
            throw new ServiceException("上传图片文件为空");
        }

        // 生成文件id
        String pictureId = IdUtils.pictureId(mFile.getOriginalFilename());
        picture.setPictureId(pictureId);
        if (picture.getPicName() == null) {
            picture.setPicName(mFile.getOriginalFilename());
        }

        // 上传OSS
        String pictureUrl = OSSUtil.upload(mFile, FileDirType.PICTURE, pictureId);

        picture.setUrl(pictureUrl);
        picture.setCreateTime(DateUtils.getNowDate());
        picture.setCreateBy(this.getUsername());
        picture.setStatus(2); // 设置为发布状态，管理员上传的图片不用审核
        this.pictureService.savePicture(picture);

        return R.success("上传图片成功");
    }

    @ApiOperation(value = "审核图片")
    @PostMapping(value = "/audit/{pictureId}/{result}")
    public R<String> audit(@PathVariable Long pictureId, @PathVariable Integer result) {
        Picture picture = this.pictureService.selectById(pictureId);
        if (result == 1) {
            // 传输图片的本地地址给 Python 程序，返回 OSS url地址

        }
        return R.success("审核成功");
    }

    /**
     * 调用 Python 程序，上传本地到 OSS，解析图片向量保存到es
     */
    public static void executePython() {
        try {
            // 参数1：解释器地址 参数二：Python文件地址 参数三：上传图片地址
            String[] args1 = new String[] {PictureConstant.INTERPRETER, PictureConstant.OFFLINE};
            Process proc = Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            proc.waitFor();
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
