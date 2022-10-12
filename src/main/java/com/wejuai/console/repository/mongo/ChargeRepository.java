package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.trade.Charge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChargeRepository extends MongoRepository<Charge, String> {
}
