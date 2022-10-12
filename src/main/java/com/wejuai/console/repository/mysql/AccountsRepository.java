package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts, String> {
}
