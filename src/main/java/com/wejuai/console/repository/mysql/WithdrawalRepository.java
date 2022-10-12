package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, String>, JpaSpecificationExecutor<Withdrawal> {
}
