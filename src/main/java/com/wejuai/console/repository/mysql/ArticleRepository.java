package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    @Modifying
    @Query(value = "update article set del=1 where user_id=?1", nativeQuery = true)
    int delByUser(String userId);
}
