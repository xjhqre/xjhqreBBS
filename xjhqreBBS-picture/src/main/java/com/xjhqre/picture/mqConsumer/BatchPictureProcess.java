package com.xjhqre.picture.mqConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
@RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_B) // 监听的队列名称
public class BatchPictureProcess {

    @Autowired
    PictureService pictureService;

    @RabbitHandler
    public void process(String[] pictureIds) {
        log.info("批量处理被调用");
        log.info(Arrays.toString(pictureIds));
        List<Picture> pictures = this.pictureService.selectBatch(pictureIds);
        List<String> urls = pictures.stream().map(Picture::getUrl).collect(Collectors.toList());
        log.info(Arrays.toString(pictureIds));
        log.info(urls.toString());
        String response = this.executePython(Arrays.asList(pictureIds), urls);
        if (response.contains("200")) {
            // 审核通过
            log.info("图片处理成功");
            for (Picture picture : pictures) {
                picture.setStatus(PictureConstant.PASS);
            }
        } else {
            // 审核出现异常，回到待审核状态
            log.info("图片处理失败");
            for (Picture picture : pictures) {
                picture.setStatus(PictureConstant.TO_BE_REVIEWED);
            }
        }
        this.pictureService.updateBatchById(pictures);
    }

    /**
     * 调用 Python 程序，上传本地到 OSS，解析图片向量保存到es
     *
     * @param ids
     *            图片id
     *
     * @param urls
     *            图片OSS地址
     */
    public String executePython(List<String> ids, List<String> urls) {
        StringBuilder response = new StringBuilder();
        try {
            // 参数1：解释器地址 参数二：Python程序地址 参数三：图片id 参数四：图片OSS地址。调用Python上传图片特征
            String[] args =
                new String[] {PictureConstant.INTERPRETER, PictureConstant.OFFLINE_2, ids.toString(), urls.toString()};
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