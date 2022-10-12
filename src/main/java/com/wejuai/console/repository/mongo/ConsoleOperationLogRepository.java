package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.ConsoleOperationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsoleOperationLogRepository extends MongoRepository<ConsoleOperationLog, String> {
}
