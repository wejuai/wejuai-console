package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.statistics.ChargeStatisticsByDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChargeStatisticsByDayRepository extends MongoRepository<ChargeStatisticsByDay, String> {

    List<ChargeStatisticsByDay> findByDateBetween(LocalDate start, LocalDate end);

    Page<ChargeStatisticsByDay> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
