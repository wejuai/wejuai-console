package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface ImageRepository extends JpaRepository<Image, String> {
}
