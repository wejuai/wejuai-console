package com.wejuai.console.service;

import com.wejuai.console.repository.mysql.AudioRepository;
import com.wejuai.console.repository.mysql.ImageRepository;
import com.wejuai.console.repository.mysql.VideoRepository;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.ImageUploadType;
import org.springframework.stereotype.Service;

/**
 * @author ZM.Wang
 */
@Service
public class MediaService {

    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final AudioRepository audioRepository;

    public MediaService(ImageRepository imageRepository, VideoRepository videoRepository, AudioRepository audioRepository) {
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
        this.audioRepository = audioRepository;
    }

    public Image createImage(String ossKey) {
        return imageRepository.save(new Image(ossKey, ImageUploadType.SYS_IMAGE));
    }

}
