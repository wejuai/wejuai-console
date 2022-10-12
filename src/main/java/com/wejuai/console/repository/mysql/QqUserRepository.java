package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.QqUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QqUserRepository extends JpaRepository<QqUser, String> {
}
