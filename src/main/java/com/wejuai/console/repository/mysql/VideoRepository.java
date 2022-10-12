package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, String> {
    Video findByOssKey(String ossKey);
}
