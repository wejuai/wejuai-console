package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.RewardDemand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RewardDemandRepository extends JpaRepository<RewardDemand, String>, JpaSpecificationExecutor<RewardDemand> {

    @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
    @Modifying
    @Query(nativeQuery = true, value = "update reward_demand set del=true where user_id=?1")
    int delByUser(String userId);
}
