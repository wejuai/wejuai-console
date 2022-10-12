package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.HobbyTab;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface HobbyTabRepository extends MongoRepository<HobbyTab, String> {

    List<HobbyTab> findByTab(String tab);

    List<HobbyTab> findByTabLike(String tab);

    Set<HobbyTab> findByHobbyId(String hobbyId);
}
