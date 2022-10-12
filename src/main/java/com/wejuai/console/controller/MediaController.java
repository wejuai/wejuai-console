package com.wejuai.console.controller;

import com.endofmaster.commons.aliyun.oss.AliyunOss;
import com.endofmaster.commons.aliyun.oss.UploadCredentials;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.config.AliyunConfig;
import com.wejuai.console.controller.dto.SystemImageType;
import com.wejuai.console.service.MediaService;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.ImageUploadType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

import static com.wejuai.dto.Constant.DATE_FORMAT4;
import static com.wejuai.entity.mysql.ImageUploadType.SYS_IMAGE;


/**
 * @author ZM.Wang
 */
@Api(tags = "媒体相关")
@RestController
@RequestMapping
public class MediaController {

    private final static Logger logger = LoggerFactory.getLogger(MediaController.class);

    private final static int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 允许的图片大小
    private final static int MAX_VIDEO_SIZE = 10 * 1024 * 1024; // 允许的视频大小
    private final static int MAX_AUDIO_SIZE = 2 * 1024 * 1024; // 允许的音频大小
    private final static int EXPIRATION = 100; // 上传凭证有效期，单位秒
    private final AliyunOss aliyunOss;
    private final AliyunConfig.OssProperties ossProperties;
    private final MediaService mediaService;

    public MediaController(AliyunOss aliyunOss, AliyunConfig.OssProperties ossProperties, MediaService mediaService) {
        this.aliyunOss = aliyunOss;
        this.ossProperties = ossProperties;
        this.mediaService = mediaService;
    }

    @ApiOperation("获取上传系统图片凭证")
    @GetMapping("/api/images/system/credentials/{type}")
    public UploadCredentials getUploadImageCredentials(@PathVariable SystemImageType type) {
        UploadCredentials credentials = aliyunOss.buildUploadCredentialsFullKey(
                type.getOssKeyPath() + DateFormatUtils.format(new Date(), DATE_FORMAT4) + ".png",
                ossProperties.getImageCallbackUrl(), MAX_IMAGE_SIZE, EXPIRATION);
        logger.debug("阿里云oss上传参数：" + credentials.toString());
        return credentials;
    }

    @ApiOperation("获取上传图片凭证")
    @GetMapping("/api/images/credentials/{type}")
    public UploadCredentials getUploadImageCredentials(@PathVariable ImageUploadType type) {
        if (type == SYS_IMAGE) {
            throw new BadRequestException("这是不允许的哦~");
        }
        UploadCredentials credentials = aliyunOss.buildUploadCredentials("image/" + type + "/system",
                ossProperties.getImageCallbackUrl(), MAX_IMAGE_SIZE, EXPIRATION);
        logger.debug("阿里云oss上传参数：" + credentials.toString());
        return credentials;
    }

    @ApiOperation(value = "图片上传回调", hidden = true)
    @PostMapping("/images/callback")
    public Map<String, Object> onImageUploaded(String ossKey, Integer size, String mimeType) {
        logger.debug("收到阿里云oss图片上传回调，ossKey={}，size={}，mimeType={}", ossKey, size, mimeType);
        Image image = mediaService.createImage(ossKey);
        return aliyunOss.buildUploadResponse(image.getId(), ossKey, size, mimeType, false);
    }

}
