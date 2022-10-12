package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.ConsoleLoginLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConsoleLoginLogRepository extends MongoRepository<ConsoleLoginLog, String> {

    List<ConsoleLoginLog> findTop2ByOrderByCreatedAtDesc();
}
