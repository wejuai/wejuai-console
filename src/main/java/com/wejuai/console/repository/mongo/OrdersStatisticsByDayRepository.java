package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.statistics.OrdersStatisticsByDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrdersStatisticsByDayRepository extends MongoRepository<OrdersStatisticsByDay, String> {

    List<OrdersStatisticsByDay> findByDateBetween(LocalDate start, LocalDate end);

    Page<OrdersStatisticsByDay> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
