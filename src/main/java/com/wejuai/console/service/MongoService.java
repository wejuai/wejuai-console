package com.wejuai.console.service;

import com.wejuai.console.service.dto.MongoCount;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZM.Wang
 */
@Service
public class MongoService {

    private final MongoTemplate mongoTemplate;

    public MongoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    long getMongoPageCount(Criteria criteria, Class<?> clazz) {
        Aggregation countAggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group().count().as("count")
        );
        AggregationResults<MongoCount> countAggregate = mongoTemplate.aggregate(countAggregation, clazz, MongoCount.class);
        MongoCount mongoCount = countAggregate.getUniqueMappedResult();
        return mongoCount == null ? 0 : mongoCount.getCount();
    }

    @SuppressWarnings("SameParameterValue")
    <T> List<T> getList(Criteria criteria, long page, long size, Class<T> clazz, Sort.Direction sort, String... sortParam) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(sort, sortParam),
                Aggregation.skip(page * size),
                Aggregation.limit(size)
        );
        AggregationResults<T> aggregate = mongoTemplate.aggregate(aggregation, clazz, clazz);
        return aggregate.getMappedResults();
    }

}
