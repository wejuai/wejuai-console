package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface HobbyRepository extends JpaRepository<Hobby, String> {

    boolean existsByName(String name);

    boolean existsByNameLike(String name);

    @Query(nativeQuery = true, value = "select id `key`,`name` `value` from hobby")
    List<Map<String, Object>> getIdAndNameList();
}
