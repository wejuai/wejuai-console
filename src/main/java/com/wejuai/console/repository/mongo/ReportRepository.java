package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
}
