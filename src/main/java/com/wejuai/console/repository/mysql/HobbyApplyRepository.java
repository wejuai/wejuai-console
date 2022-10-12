package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.HobbyApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HobbyApplyRepository extends JpaRepository<HobbyApply, String>, JpaSpecificationExecutor<HobbyApply> {

    Page<HobbyApply> findByHandle(boolean handle, Pageable pageable);
}
