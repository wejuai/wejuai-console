package com.wejuai.console.repository.mysql;

import com.wejuai.entity.mysql.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, String> {
}
