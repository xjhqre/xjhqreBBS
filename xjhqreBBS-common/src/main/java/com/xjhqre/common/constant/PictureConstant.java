package com.xjhqre.common.constant;

/**
 * <p>
 * ConfigConstant
 * </p>
 *
 * @author xjhqre
 * @since 10月 24, 2022
 */
public class PictureConstant {

    // TODO 绝对路径
    public static final String INTERPRETER = "G:\\Miniconda3\\envs\\sis3.7\\python.exe";
    // 解析向量文件路径
    public static final String FEATURES =
            "G:\\workspace\\xjhqreBBS\\xjhqreBBS-common\\src\\main\\java\\com\\xjhqre\\common\\python\\feature_extractor.py";
    // upload.py
    public static final String OFFLINE =
            "G:\\workspace\\xjhqreBBS\\python\\upload.py";
    public static final String OFFLINE_2 =
            "G:\\workspace\\xjhqreBBS\\python\\batchUpload.py";

    // 待审核，用户提交后图片的状态。
    public static final Integer TO_BE_REVIEWED = 0;
    // 审核中，异步执行Python程序时的状态，上传es成功后变成已入库，若未上传成功则变为待审核
    public static final Integer PROCESSING = 1;
    // 已入库，审核通过且上传成功的图片
    public static final Integer PASS = 2;
    // 未通过，审核没通过的图片，经用户修改后再次变为待审核
    public static final Integer FAILED = 3;

}
