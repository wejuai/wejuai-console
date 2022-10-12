package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.ConsoleOperationRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsoleOperationRecordRepository extends MongoRepository<ConsoleOperationRecord, String> {
}
