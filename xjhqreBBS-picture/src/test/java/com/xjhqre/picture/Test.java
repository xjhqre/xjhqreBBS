package com.xjhqre.picture;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.xjhqre.picture.utils.ElasticSearchUtils;

/**
 * <p>
 * Test
 * </p>
 *
 * @author xjhqre
 * @since 10月 24, 2022
 */
public class Test {
    @Autowired
    ElasticSearchUtils elasticSearchUtils;

    @org.junit.jupiter.api.Test
    public void Test1() throws IOException {
        this.elasticSearchUtils.createIndex("test");
    }
}
