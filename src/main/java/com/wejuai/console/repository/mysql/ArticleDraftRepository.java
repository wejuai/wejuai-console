package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.ArticleDraft;
import com.wejuai.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface ArticleDraftRepository extends JpaRepository<ArticleDraft, String> {

    void deleteByUser(User user);
}
