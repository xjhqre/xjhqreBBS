package com.xjhqre.sms;

import com.xjhqre.common.domain.picture.Picture;
import com.xjhqre.sms.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Test
 * </p>
 *
 * @author xjhqre
 * @since 11月 15, 2022
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SmsApplication.class)
@Slf4j
public class Test {

    @Autowired
    PictureService pictureService;

    @org.junit.Test
    public void test1() {
        String[] pictureIds = {"5fabbae85798413caa79e9b9878ea6ee", "80f2ccef113c48d9aded3370f43bb445"};
        log.info("批量处理被调用");
        List<Picture> pictures = this.pictureService.selectBatch(pictureIds);
        List<String> urlList = pictures.stream().map(Picture::getUrl).collect(Collectors.toList());
        String urls = String.join(",", urlList);
        String ids = String.join(",", pictureIds);
        log.info(ids);
        log.info(urls);
    }
}
