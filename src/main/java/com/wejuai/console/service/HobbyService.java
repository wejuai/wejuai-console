package com.wejuai.console.service;

import com.endofmaster.commons.id.IdGenerator;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.request.SaveHobbyRequest;
import com.wejuai.console.controller.dto.request.UpdateHobbyTabsRequest;
import com.wejuai.console.controller.dto.request.UpdateOneValueRequest;
import com.wejuai.console.controller.dto.response.HobbyDetailedInfo;
import com.wejuai.console.controller.dto.response.HobbyListInfo;
import com.wejuai.console.repository.mongo.HobbyHotRepository;
import com.wejuai.console.repository.mongo.HobbyTabRepository;
import com.wejuai.console.repository.mysql.HobbyRepository;
import com.wejuai.console.repository.mysql.ImageRepository;
import com.wejuai.entity.mongo.CelestialBody;
import com.wejuai.entity.mongo.HobbyTab;
import com.wejuai.entity.mongo.statistics.HobbyHot;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.ImageUploadType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author ZM.Wang
 */
@Service
public class HobbyService {

    private final static Logger logger = LoggerFactory.getLogger(HobbyService.class);

    private final HobbyRepository hobbyRepository;
    private final ImageRepository imageRepository;
    private final HobbyHotRepository hobbyHotRepository;
    private final HobbyTabRepository hobbyTabRepository;

    private final CelestialBodyService celestialBodyService;

    public HobbyService(HobbyRepository hobbyRepository, HobbyHotRepository hobbyHotRepository, HobbyTabRepository hobbyTabRepository, ImageRepository imageRepository, CelestialBodyService celestialBodyService) {
        this.hobbyRepository = hobbyRepository;
        this.hobbyHotRepository = hobbyHotRepository;
        this.hobbyTabRepository = hobbyTabRepository;
        this.imageRepository = imageRepository;
        this.celestialBodyService = celestialBodyService;
    }

    public List<Map<String, Object>> hobbySelectList() {
        return hobbyRepository.getIdAndNameList();
    }

    public Page<HobbyListInfo> getHobbies(Pageable pageable) {
        return hobbyRepository.findAll(pageable).map(hobby -> {
            HobbyHot hobbyHot = getHobbyHot(hobby.getId());
            return new HobbyListInfo(hobby, hobbyHot);
        });
    }

    public HobbyDetailedInfo getHobby(String id) {
        Hobby hobby = hobbyRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该爱好: " + id));
        Set<HobbyTab> hobbyTabs = hobbyTabRepository.findByHobbyId(id);
        HobbyHot hobbyHot = getHobbyHot(id);
        CelestialBody celestialBody = celestialBodyService.getCelestialBodyByHobby(id);
        return new HobbyDetailedInfo(hobby, hobbyHot, celestialBody, hobbyTabs);
    }

    @Transactional
    public void addHobby(SaveHobbyRequest request) {
        Optional<Hobby> hobbyOptional = hobbyRepository.findById(request.getId());
        if (hobbyOptional.isPresent()) {
            throw new BadRequestException("已有该爱好id: " + request.getId());
        }
        if (hobbyRepository.existsByName(request.getName())) {
            throw new BadRequestException("该爱好名称重复: " + request.getName());
        }
        if (hobbyRepository.existsByNameLike(request.getName())) {
            throw new BadRequestException("该爱好名称有类似: " + request.getName());
        }
        Image avatar = imageRepository.findById(request.getAvatar()).orElseThrow(() -> new BadRequestException("没有该图片: " + request.getAvatar()));
        if (avatar.getType() != ImageUploadType.SYS_IMAGE) {
            throw new BadRequestException("图片类型错误: " + avatar.getType());
        }
        hobbyRepository.save(new Hobby(request.getId(), request.getName()).setAvatar(avatar));
        getHobbyHot(request.getId());
        for (String tab : request.getTabs()) {
            checkTab(tab);
            hobbyTabRepository.save(new HobbyTab(IdGenerator.objectId(), tab, request.getId()));
        }
    }

    public void updateAvatar(String hobbyId, UpdateOneValueRequest request) {
        Hobby hobby = hobbyRepository.findById(hobbyId).orElseThrow(() -> new BadRequestException("没有该爱好: " + hobbyId));
        Image avatar = imageRepository.findById(request.getValue()).orElseThrow(() -> new BadRequestException("没有该图片: " + request.getValue()));
        if (avatar.getType() != ImageUploadType.SYS_IMAGE) {
            throw new BadRequestException("图片类型错误: " + avatar.getType());
        }
        hobbyRepository.save(hobby.setAvatar(avatar));
    }

    public void addTabs(String hobbyId, String tab) {
        if (!hobbyRepository.existsById(hobbyId)) {
            throw new BadRequestException("没有该爱好: " + hobbyId);
        }
        tab = tab.toUpperCase();
        checkTab(tab);
        hobbyTabRepository.save(new HobbyTab(IdGenerator.objectId(), tab, hobbyId));
    }

    public void subTabs(String hobbyId, UpdateHobbyTabsRequest request) {
        if (!hobbyRepository.existsById(hobbyId)) {
            throw new BadRequestException("没有该爱好: " + hobbyId);
        }
        Optional<HobbyTab> byId = hobbyTabRepository.findById(request.getTab());
        if (byId.isEmpty()) {
            return;
        }
        if (!StringUtils.equals(byId.get().getHobbyId(), hobbyId)) {
            logger.warn("该tab不属于这个爱好, hobby: {}, tab: {}", hobbyId, byId.get());
            return;
        }
        hobbyTabRepository.deleteById(request.getTab());
    }

    private void checkTab(String tab) {
        List<HobbyTab> byTab = hobbyTabRepository.findByTab(tab);
        if (byTab.size() > 0) {
            throw new BadRequestException("该爱好标签重复: " + tab);
        }
        List<HobbyTab> byTabLike = hobbyTabRepository.findByTabLike(tab);
        if (byTabLike.size() > 0) {
            throw new BadRequestException("该爱好标签有类似: " + tab);
        }
    }

    private HobbyHot getHobbyHot(String hobbyId) {
        HobbyHot hobbyHot = hobbyHotRepository.findByHobbyId(hobbyId);
        if (hobbyHot == null) {
            hobbyHot = hobbyHotRepository.save(new HobbyHot(hobbyId));
        }
        return hobbyHot;
    }

}
