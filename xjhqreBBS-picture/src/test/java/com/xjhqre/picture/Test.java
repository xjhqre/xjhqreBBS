package com.xjhqre.picture;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xjhqre.picture.listener.PictureProcessListener;

/**
 * <p>
 * Test
 * </p>
 *
 * @author xjhqre
 * @since 10月 24, 2022
 */
@SpringBootTest
@RunWith(SpringRunner.class)
// @ContextConfiguration(classes = CloudServiceApp.class)
public class Test {

    @Autowired
    PictureProcessListener processing;

    @org.junit.Test
    public void test1() {
        this.processing.process("3a8ee2cc23f34edb8d8916285740fe9b");
    }
}
