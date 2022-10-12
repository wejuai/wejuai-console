package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.WeiboUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeiboUserRepository extends JpaRepository<WeiboUser, String> {
}
