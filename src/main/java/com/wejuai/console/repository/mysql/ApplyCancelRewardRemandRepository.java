package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.ApplyCancelRewardRemand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author ZM.Wang
 */
public interface ApplyCancelRewardRemandRepository extends JpaRepository<ApplyCancelRewardRemand, String>, JpaSpecificationExecutor<ApplyCancelRewardRemand> {

}
