package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.request.AppRevokeRequest;
import com.wejuai.console.controller.dto.response.*;
import com.wejuai.console.service.RewardDemandService;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.response.RewardDemandInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.RewardDemandStatus;
import com.wejuai.entity.mysql.RewardSubmission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
@Api(tags = "悬赏相关")
@RestController
@RequestMapping("/api/rewardDemand")
public class RewardDemandController {

    private final RewardDemandService rewardDemandService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public RewardDemandController(RewardDemandService rewardDemandService, WejuaiCoreClient wejuaiCoreClient) {
        this.rewardDemandService = rewardDemandService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("悬赏列表")
    @GetMapping
    public Slice<AppListInfo> rewardDemands(@RequestParam(required = false, defaultValue = "") String id,
                                            @RequestParam(required = false, defaultValue = "") String userId,
                                            @RequestParam(required = false, defaultValue = "") String hobbyId,
                                            @RequestParam(required = false, defaultValue = "") Boolean del,
                                            @RequestParam(required = false, defaultValue = "") RewardDemandStatus status,
                                            @RequestParam(required = false, defaultValue = "")
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                            @RequestParam(required = false, defaultValue = "")
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return rewardDemandService.rewardDemands(id, userId, hobbyId, del, status, start, end, pageable);
    }

    @ApiOperation("悬赏详情")
    @GetMapping("/{id}")
    public RewardDemandInfo getRewardDemand(@PathVariable String id) {
        return rewardDemandService.getRewardDemand(id);
    }

    @ApiOperation("悬赏回答")
    @GetMapping("/{rewardDemandId}/rewardSubmissions")
    public Page<RewardSubmissionBaseInfo> rewardSubmissions(@PathVariable String rewardDemandId,
                                                            @RequestParam(required = false, defaultValue = "") String userId,
                                                            @RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return rewardDemandService.rewardSubmissions(rewardDemandId, userId, pageable);
    }

    @ApiOperation("获取回答详情")
    @GetMapping("/rewardSubmission/{id}")
    public RewardSubmissionInfo getRewardSubmission(@PathVariable String id) {
        RewardSubmission rewardSubmission = rewardDemandService.getRewardSubmission(id);
        return new RewardSubmissionInfo(rewardSubmission);
    }

    @ApiOperation("删除悬赏")
    @DeleteMapping("/{id}")
    public void delOrRecoveryRewardDemand(@PathVariable String id,
                                          @RequestParam(required = false, defaultValue = "") String reason) {
        rewardDemandService.delOrRecoveryRewardDemand(id, reason);
    }

    @ApiOperation("延时悬赏")
    @PutMapping("/{id}/extension")
    public void rewardDemandExtension(@PathVariable String id) {
        wejuaiCoreClient.rewardDemandExtension(id);
    }

    @ApiOperation("撤回悬赏回答")
    @PostMapping("/rewardSubmission/{id}/revoke")
    public void revokeRewardSubmission(@PathVariable String id, @RequestBody @Valid AppRevokeRequest request) {
        wejuaiCoreClient.revokeRewardDemand(id, request.getReason());
    }

    @ApiOperation("申请返还全额悬赏积分列表")
    @GetMapping("/applyCancel")
    public Page<ApplyCancelRewardRemandListInfo> getApplyCancelRewardDemands(@RequestParam(required = false, defaultValue = "") ApplyStatus applyStatus,
                                                                             @RequestParam(required = false, defaultValue = "") String userId,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                             @RequestParam(required = false, defaultValue = "") LocalDate start,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                             @RequestParam(required = false, defaultValue = "") LocalDate end,
                                                                             @RequestParam(required = false, defaultValue = "0") int page,
                                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        return rewardDemandService.getApplyCancelRewardDemands(applyStatus, userId, start, end, pageable);
    }

    @ApiOperation("申请返还全额悬赏积分详情")
    @GetMapping("/applyCancel/{id}")
    public ApplyCancelRewardRemandInfo getApplyCancelRewardRemandInfo(@PathVariable String id) {
        return rewardDemandService.getApplyCancelRewardRemandInfo(id);
    }

    @ApiOperation("通过全额返还悬赏积分")
    @PutMapping("/applyCancel/{id}/pass")
    public void passApplyCancelRewardRemand(@PathVariable String id) {
        rewardDemandService.passApplyCancelRewardRemand(id);
    }

    @ApiOperation("驳回全额返还悬赏积分")
    @PutMapping("/applyCancel/{id}/unPass")
    public void unPassApplyCancelRewardRemand(@PathVariable String id, @RequestParam String rejectionReason) {
        rewardDemandService.unPassApplyCancelRewardRemand(id, rejectionReason);
    }
}
