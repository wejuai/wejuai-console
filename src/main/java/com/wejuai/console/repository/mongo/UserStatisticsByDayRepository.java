package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.statistics.UserStatisticsByDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserStatisticsByDayRepository extends MongoRepository<UserStatisticsByDay, String> {

    List<UserStatisticsByDay> findByDateBetween(LocalDate start, LocalDate end);

    Page<UserStatisticsByDay> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
