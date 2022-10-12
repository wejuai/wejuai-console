package com.wejuai.console.controller;

import com.wejuai.console.service.CelestialBodyService;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.response.CelestialBodyInfo;
import com.wejuai.dto.response.RefreshUserIntegralInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.CelestialBodyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZM.Wang
 */
@Api(tags = "星球管理")
@RestController
@RequestMapping("/api/celestialBody")
public class CelestialBodyController {

    private final WejuaiCoreClient wejuaiCoreClient;
    private final CelestialBodyService celestialBodyService;

    public CelestialBodyController(WejuaiCoreClient wejuaiCoreClient, CelestialBodyService celestialBodyService) {
        this.wejuaiCoreClient = wejuaiCoreClient;
        this.celestialBodyService = celestialBodyService;
    }

    @ApiOperation("星球列表")
    @GetMapping
    public Slice<CelestialBodyInfo> getCelestialBodies(@RequestParam(required = false, defaultValue = "") CelestialBodyType type,
                                                       @RequestParam(required = false, defaultValue = "") String typeId,
                                                       @RequestParam(required = false, defaultValue = "") String id,
                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return celestialBodyService.getCelestialBodies(id, type, typeId, page, size);
    }

    @ApiOperation("修改点数")
    @PutMapping("/{id}/point/{point}")
    public void updateCelestialBodyPoint(@PathVariable String id, @PathVariable @ApiParam("点数，增加是正数，减少是负数") long point) {
        celestialBodyService.updateCelestialBodyPoint(id, point);
    }

    @ApiOperation("同步用户点数")
    @PostMapping("/sync/user/{userId}")
    public RefreshUserIntegralInfo syncUserPoint(@PathVariable String userId) {
        return wejuaiCoreClient.syncUserPoint(userId);
    }

    @ApiOperation("重新创建没有星球的爱好和用户的星球")
    @PostMapping("/sync/notHave")
    public void syncNotHave() {
        celestialBodyService.syncNotHave();
    }
}
