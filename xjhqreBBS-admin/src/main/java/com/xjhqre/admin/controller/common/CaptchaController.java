package com.xjhqre.admin.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.CacheConstants;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.service.ConfigService;
import com.xjhqre.common.utils.redis.RedisCache;
import com.xjhqre.common.utils.sign.Base64;
import com.xjhqre.common.utils.uuid.IdUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/***
 * 验证码操作处理**
 * 
 * @author xjhqre
 */
@RestController
@Api(value = "验证码接口", tags = "验证码接口")
public class CaptchaController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ConfigService configService;

    /**
     * 生成验证码
     */
    @ApiOperation(value = "生成验证码")
    @GetMapping("/captchaImage")
    public R<String> getCode() {
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        if (!captchaEnabled) {
            return R.error("验证码未开启");
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = this.configService.selectConfigByKey("captchaType");
        if ("math".equals(captchaType)) {
            String capText = this.captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = this.captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = this.captchaProducer.createText();
            image = this.captchaProducer.createImage(capStr);
        }

        this.redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return R.error(e.getMessage());
        }
        return R.success("生成验证码成功").add("uuid", uuid).add("img", Base64.encode(os.toByteArray()));
    }
}
