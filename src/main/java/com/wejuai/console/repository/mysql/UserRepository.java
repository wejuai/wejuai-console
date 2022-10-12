package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @author ZM.Wang
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {


    @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
    @Query(value = "select id from `user`", nativeQuery = true)
    Set<String> findAllUserId();

    @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
    @Modifying
    @Query(value = "update `user` set msg_num=msg_num+1", nativeQuery = true)
    void addAllUserMsgNum();

    @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
    @Modifying
    @Query(value = "update `user` set msg_num=msg_num+1 where id=?1", nativeQuery = true)
    void addUserMsgNum(String userId);
}
