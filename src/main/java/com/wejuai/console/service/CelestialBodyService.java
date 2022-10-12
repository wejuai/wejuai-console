package com.wejuai.console.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.NotFoundException;
import com.wejuai.console.repository.mongo.CelestialBodyRepository;
import com.wejuai.console.repository.mongo.UserPointRecordRepository;
import com.wejuai.console.repository.mysql.HobbyRepository;
import com.wejuai.console.repository.mysql.UserRepository;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.request.SaveCelestialBodyRequest;
import com.wejuai.dto.response.CelestialBodyInfo;
import com.wejuai.dto.response.IdBaseResponse;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.CelestialBody;
import com.wejuai.entity.mongo.CelestialBodyType;
import com.wejuai.entity.mongo.UserPointRecord;
import com.wejuai.entity.mongo.UserPointType;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.wejuai.entity.mongo.CelestialBodyType.HOBBY;
import static com.wejuai.entity.mongo.CelestialBodyType.UNOWNED;
import static com.wejuai.entity.mongo.CelestialBodyType.USER;

/**
 * @author ZM.Wang
 * 星球服务
 */
@Service
public class CelestialBodyService {

    private final UserRepository userRepository;
    private final HobbyRepository hobbyRepository;
    private final CelestialBodyRepository celestialBodyRepository;
    private final UserPointRecordRepository userPointRecordRepository;

    private final WejuaiCoreClient wejuaiCoreClient;
    private final MongoService mongoService;

    public CelestialBodyService(UserRepository userRepository, HobbyRepository hobbyRepository, CelestialBodyRepository celestialBodyRepository, UserPointRecordRepository userPointRecordRepository, WejuaiCoreClient wejuaiCoreClient, MongoService mongoService) {
        this.userRepository = userRepository;
        this.hobbyRepository = hobbyRepository;
        this.celestialBodyRepository = celestialBodyRepository;
        this.userPointRecordRepository = userPointRecordRepository;
        this.wejuaiCoreClient = wejuaiCoreClient;
        this.mongoService = mongoService;
    }

    public Slice<CelestialBodyInfo> getCelestialBodies(String id, CelestialBodyType type, String typeId, long page, long size) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(id)) {
            criteria.and("id").is(id);
        } else {
            if (type != null) {
                criteria.and("type").is(type);
            }
            if (type == USER) {
                criteria.and("user").is(typeId);
            }
            if (type == HOBBY) {
                criteria.and("hobby").is(typeId);
            }
        }
        List<CelestialBody> list = mongoService.getList(criteria, page, size, CelestialBody.class, Sort.Direction.DESC, "createdAt");
        List<CelestialBodyInfo> celestialBodyInfos = list.stream().map(celestialBody -> {
            if (celestialBody.getType() == HOBBY) {
                Hobby hobby = hobbyRepository.findById(celestialBody.getHobby()).orElse(null);
                return new CelestialBodyInfo(celestialBody).hobbyInfo(hobby);
            } else if (celestialBody.getType() == USER) {
                User user = userRepository.findById(celestialBody.getUser()).orElse(null);
                return new CelestialBodyInfo(celestialBody).userInfo(user);
            } else {
                return new CelestialBodyInfo(celestialBody);
            }
        }).collect(Collectors.toList());
        long count = mongoService.getMongoPageCount(criteria, CelestialBody.class);
        return new Slice<>(celestialBodyInfos, page, size, count);
    }

    /** 重新创建没有星球的爱好和用户 */
    public void syncNotHave() {
        userRepository.findAll().forEach(user -> {
            wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(UNOWNED, null));
            wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(USER, user.getId()));
            wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(UNOWNED, null));
        });
        hobbyRepository.findAll().forEach(hobby -> {
            wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(UNOWNED, null));
            wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(HOBBY, hobby.getId()));
            wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(UNOWNED, null));
        });
    }

    public CelestialBody getCelestialBodyByHobby(String hobbyId) {
        hobbyRepository.findById(hobbyId).orElseThrow(() -> new BadRequestException("没有找到该星球对应爱好: " + hobbyId));
        CelestialBody celestialBody = celestialBodyRepository.findByHobby(hobbyId);
        if (celestialBody == null) {
            celestialBody = saveCelestialBody(HOBBY, hobbyId);
        }
        return celestialBody;
    }

    public CelestialBody saveCelestialBody(CelestialBodyType type, String id) {
        IdBaseResponse idBaseResponse = wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(type, id));
        return celestialBodyRepository.findById(idBaseResponse.getId()).orElseThrow(() -> new NotFoundException("创建星球失败"));
    }

    public void updateCelestialBodyPoint(String id, long point) {
        CelestialBody celestialBody = celestialBodyRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该星球: " + id));
        if (point > 0) {
            celestialBodyRepository.save(celestialBody.addPoint(point));
        } else {
            celestialBodyRepository.save(celestialBody.cutPoint(-point));
        }
        if (celestialBody.getType() == USER) {
            userPointRecordRepository.save(new UserPointRecord(celestialBody.getUser(), point > 0 ? UserPointType.SYSTEM_ADD : UserPointType.SYSTEM_SUB, point));
        }
    }

}
