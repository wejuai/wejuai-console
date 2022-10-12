package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mongo.CelestialBody;
import com.wejuai.entity.mongo.CelestialBodyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CelestialBodyRepository extends MongoRepository<CelestialBody, String> {

    CelestialBody findByUser(String userId);

    CelestialBody findByHobby(String hobbyId);

    List<CelestialBody> findByXBetweenAndYBetween(long minX, long maxX, long minY, long maxY);

    Page<CelestialBody> findByType(CelestialBodyType type, Pageable pageable);
}
