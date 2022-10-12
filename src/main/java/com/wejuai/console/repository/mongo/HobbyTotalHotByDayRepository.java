package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.statistics.HobbyTotalHotByDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface HobbyTotalHotByDayRepository extends MongoRepository<HobbyTotalHotByDay, String> {

    List<HobbyTotalHotByDay> findByDateBetween(LocalDate start, LocalDate end);

    Page<HobbyTotalHotByDay> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
