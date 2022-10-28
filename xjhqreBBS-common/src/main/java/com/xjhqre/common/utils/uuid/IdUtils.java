package com.xjhqre.common.utils.uuid;

import org.apache.commons.io.FilenameUtils;

/**
 * ID生成器工具类
 * 
 * @author xjqhre
 */
public class IdUtils {

    /**
     * 简化的UUID，去掉了横线
     * 
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     * 
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 获取pictureId
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String pictureId(String originalFilename) {
        return String.format("%s.%s", IdUtils.simpleUUID(), FilenameUtils.getExtension(originalFilename));
    }

}
