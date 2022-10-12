package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.RewardDemand;
import com.wejuai.entity.mysql.RewardSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RewardSubmissionRepository extends JpaRepository<RewardSubmission, String>, JpaSpecificationExecutor<RewardSubmission> {

    List<RewardSubmission> findByRewardDemand(RewardDemand rewardDemand);

}
