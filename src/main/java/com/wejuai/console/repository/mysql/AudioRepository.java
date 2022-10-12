package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, String> {

    Audio findByOssKey(String ossKey);
}
