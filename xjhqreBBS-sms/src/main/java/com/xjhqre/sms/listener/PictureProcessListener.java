package com.xjhqre.sms.listener;

import com.xjhqre.common.config.RabbitMQConfig;
import com.xjhqre.common.constant.PictureConstant;
import com.xjhqre.common.domain.picture.Picture;
import com.xjhqre.sms.service.PictureService;
import com.xjhqre.sms.utils.TransferPython;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PictureProcessListener {

    @Autowired
    PictureService pictureService;

    @RabbitListener(queues = RabbitMQConfig.PICTURE) // 监听的队列名称
    public void pictureProcess(String[] pictureIds) {
        log.info("图片处理被调用");
        List<Picture> pictures = this.pictureService.selectBatch(pictureIds);
        List<String> urlList = pictures.stream().map(Picture::getUrl).collect(Collectors.toList());
        String urls = String.join(",", urlList);
        String ids = String.join(",", pictureIds);
        log.info(ids);
        log.info(urls);
        String response = TransferPython.send(ids, urls);
        if (response.contains("200")) {
            log.info("图片处理成功");
            for (Picture picture : pictures) {
                picture.setStatus(PictureConstant.PASS);
            }
        } else {
            // 图片处理出现异常，回到待审核状态
            log.info("图片处理异常");
            for (Picture picture : pictures) {
                picture.setStatus(PictureConstant.ABNORMAL);
            }
        }
        this.pictureService.updateBatchById(pictures);
    }
}