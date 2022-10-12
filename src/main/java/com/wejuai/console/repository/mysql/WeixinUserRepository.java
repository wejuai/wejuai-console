package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.WeixinUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeixinUserRepository extends JpaRepository<WeixinUser, String> {
}
