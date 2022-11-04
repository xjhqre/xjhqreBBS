package com.xjhqre.picture.mqConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.config.RabbitMQConfig;
import com.xjhqre.common.constant.PictureConstant;
import com.xjhqre.common.domain.picture.Picture;
import com.xjhqre.picture.service.PictureService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_A) // 监听的队列名称
public class SinglePictureProcess {

    @Autowired
    PictureService pictureService;

    @RabbitHandler
    public void process(String pictureId) {
        Picture picture = this.pictureService.getById(pictureId);

        String response = this.executePython(pictureId, picture.getUrl());

        if (response.contains("200")) {
            // 审核通过
            log.info("图片处理成功");
            picture.setStatus(PictureConstant.PASS);
        } else {
            // 审核出现异常，回到待审核状态
            log.info("图片处理失败");
            picture.setStatus(PictureConstant.TO_BE_REVIEWED);
        }
        this.pictureService.updateById(picture);
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