package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.CancelAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CancelAccountRepository extends JpaRepository<CancelAccount, String>, JpaSpecificationExecutor<CancelAccount> {
}
