package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.UserPointRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPointRecordRepository extends MongoRepository<UserPointRecord, String> {
}
