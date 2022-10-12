package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.request.SaveHobbyRequest;
import com.wejuai.console.controller.dto.request.UpdateHobbyTabsRequest;
import com.wejuai.console.controller.dto.request.UpdateOneValueRequest;
import com.wejuai.console.controller.dto.response.HobbyDetailedInfo;
import com.wejuai.console.controller.dto.response.HobbyListInfo;
import com.wejuai.console.service.CelestialBodyService;
import com.wejuai.console.service.HobbyService;
import com.wejuai.entity.mongo.CelestialBodyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author ZM.Wang
 */
@Api(tags = "爱好管理")
@RestController
@RequestMapping("/api/hobby")
public class HobbyController {

    private final HobbyService hobbyService;
    private final CelestialBodyService celestialBodyService;

    public HobbyController(HobbyService hobbyService, CelestialBodyService celestialBodyService) {
        this.hobbyService = hobbyService;
        this.celestialBodyService = celestialBodyService;
    }

    @ApiOperation("爱好列表")
    @GetMapping
    public Page<HobbyListInfo> getHobbies(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createAt");
        return hobbyService.getHobbies(pageable);
    }

    @ApiOperation("下拉菜单使用列表")
    @GetMapping("/selected")
    public List<Map<String, Object>> hobbySelectList() {
        return hobbyService.hobbySelectList();
    }

    @ApiOperation("爱好详情")
    @GetMapping("/{id}")
    public HobbyDetailedInfo getHobby(@PathVariable String id) {
        return hobbyService.getHobby(id);
    }

    @ApiOperation("添加爱好")
    @PostMapping
    public void addHobby(@RequestBody @Valid SaveHobbyRequest request) {
        hobbyService.addHobby(request);
        celestialBodyService.saveCelestialBody(CelestialBodyType.HOBBY, request.getId());
    }

    @ApiOperation("修改爱好图片，请求值是imageId")
    @PutMapping("/{hobbyId}/avatar")
    public void updateAvatar(@PathVariable String hobbyId, @RequestBody @Valid UpdateOneValueRequest request) {
        hobbyService.updateAvatar(hobbyId, request);
    }

    @ApiOperation("添加爱好搜索标签")
    @PutMapping("/{hobbyId}/tabs/add")
    public void addTabs(@PathVariable String hobbyId, @RequestBody @Valid UpdateHobbyTabsRequest request) {
        hobbyService.addTabs(hobbyId, request.getTab());
    }

    @ApiOperation("减少爱好搜索标签")
    @PutMapping("/{hobbyId}/tabs/sub")
    public void subTabs(@PathVariable String hobbyId, @RequestBody @Valid UpdateHobbyTabsRequest request) {
        hobbyService.subTabs(hobbyId, request);
    }
}
