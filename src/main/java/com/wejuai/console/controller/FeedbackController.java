package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.response.FeedbackInfo;
import com.wejuai.console.controller.dto.response.ReportInfo;
import com.wejuai.console.service.FeedbackService;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.ReportType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author ZM.Wang
 * 反馈
 */
@Api(tags = "反馈相关")
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation("意见反馈列表")
    @GetMapping
    public Page<FeedbackInfo> getFeedbacks(@RequestParam(required = false, defaultValue = "") Boolean handle,
                                           @RequestParam(required = false, defaultValue = "") String userId,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                                           @RequestParam(required = false, defaultValue = "") LocalDate start,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                                           @RequestParam(required = false, defaultValue = "") LocalDate end,
                                           @RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        return feedbackService.getFeedbacks(handle, userId, start, end, pageable);
    }

    @ApiOperation("爱好申请")
    @GetMapping("/hobbyApply")
    public Page<FeedbackInfo> getHobbyApplies(@RequestParam(required = false, defaultValue = "") Boolean handle,
                                              @RequestParam(required = false, defaultValue = "") String userId,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd")
                                              @RequestParam(required = false, defaultValue = "") LocalDate start,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd")
                                              @RequestParam(required = false, defaultValue = "") LocalDate end,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        return feedbackService.getHobbyApplies(handle, userId, start, end, pageable);
    }

    @ApiOperation("举报列表")
    @GetMapping("/report")
    public Slice<ReportInfo> getReports(@RequestParam(required = false, defaultValue = "") ReportType type,
                                        @RequestParam(required = false, defaultValue = "") String userId,
                                        @RequestParam(required = false, defaultValue = "") String beUserId,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        @RequestParam(required = false, defaultValue = "") LocalDate start,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        @RequestParam(required = false, defaultValue = "") LocalDate end,
                                        @RequestParam(required = false, defaultValue = "") String appId,
                                        @RequestParam(required = false, defaultValue = "0") long page,
                                        @RequestParam(required = false, defaultValue = "10") long size) {
        return feedbackService.getReports(type, userId, beUserId, start, end, appId, page, size);
    }

    @ApiOperation("处理意见反馈")
    @PutMapping("/{id}")
    public void handleFeedback(@PathVariable String id, @RequestParam(required = false, defaultValue = "") String content) {
        feedbackService.handleFeedback(id, content);
    }

    @ApiOperation("处理爱好申请")
    @PutMapping("/hobbyApply/{id}")
    public void handleHobbyApply(@PathVariable String id, @RequestParam(required = false, defaultValue = "") String content) {
        feedbackService.handleHobbyApply(id, content);
    }

}
