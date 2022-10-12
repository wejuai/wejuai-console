package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.statistics.HobbyHotByDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface HobbyHotByDayRepository extends MongoRepository<HobbyHotByDay, String> {

    List<HobbyHotByDay> findByHobbyIdAndDateBetween(String hobbyId, LocalDate start, LocalDate end);

    Page<HobbyHotByDay> findByHobbyIdAndDateBetween(String hobbyId, LocalDate start, LocalDate end, Pageable pageable);

    Page<HobbyHotByDay> findByHobbyId(String hobbyId, Pageable pageable);
}
