package com.xjhqre.picture.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.xjhqre.common.constant.PictureConstant;

/**
 * <p>
 * 向量工具类，对应feature_extractor.py文件
 * </p>
 *
 * @author xjhqre
 * @since 10月 24, 2022
 */
public class FeatureExtractor {

    /**
     * 获取图片向量
     */
    public static void execute() {
        try {
            String[] args1 = new String[] {PictureConstant.INTERPRETER, PictureConstant.FEATURES};
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

    public static void main(String[] args) {
        execute();
    }
}
