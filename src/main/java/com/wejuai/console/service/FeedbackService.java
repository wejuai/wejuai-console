package com.wejuai.console.service;

import com.endofmaster.commons.util.DateUtil;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.request.SendSystemMessageRequest;
import com.wejuai.console.controller.dto.response.FeedbackInfo;
import com.wejuai.console.controller.dto.response.ReportInfo;
import com.wejuai.console.repository.mysql.FeedbackRepository;
import com.wejuai.console.repository.mysql.HobbyApplyRepository;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.Report;
import com.wejuai.entity.mongo.ReportType;
import com.wejuai.entity.mysql.Feedback;
import com.wejuai.entity.mysql.HobbyApply;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZM.Wang
 */
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final HobbyApplyRepository hobbyApplyRepository;

    private final MongoService mongoService;
    private final MessageService messageService;

    public FeedbackService(HobbyApplyRepository hobbyApplyRepository, FeedbackRepository feedbackRepository, MessageService messageService, MongoService mongoService) {
        this.hobbyApplyRepository = hobbyApplyRepository;
        this.feedbackRepository = feedbackRepository;
        this.messageService = messageService;
        this.mongoService = mongoService;
    }

    public Page<FeedbackInfo> getFeedbacks(Boolean handle, String userId, LocalDate start, LocalDate end, Pageable pageable) {
        Specification<Feedback> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (handle != null) {
                predicates.add(cb.equal(root.get("handle"), handle));
            }
            if (start != null && end != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayStart(start)));
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayEnd(end)));
            }
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return feedbackRepository.findAll(specification, pageable).map(FeedbackInfo::new);
    }

    public Page<FeedbackInfo> getHobbyApplies(Boolean handle, String userId, LocalDate start, LocalDate end, Pageable pageable) {
        Specification<HobbyApply> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (handle != null) {
                predicates.add(cb.equal(root.get("handle"), handle));
            }
            if (start != null && end != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayStart(start)));
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayEnd(end)));
            }
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return hobbyApplyRepository.findAll(specification, pageable).map(FeedbackInfo::new);
    }

    public Slice<ReportInfo> getReports(ReportType type, String userId, String beUserId, LocalDate start, LocalDate end, String appId, long page, long size) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(userId)) {
            criteria.and("userId").is(userId);
        }
        if (StringUtils.isNotBlank(beUserId)) {
            criteria.and("beUserId").is(beUserId);
        }
        if (StringUtils.isNotBlank(appId)) {
            criteria.and("appId").is(appId);
        }
        if (type != null) {
            criteria.and("type").is(type);
        }
        if (start != null) {
            criteria.and("createdAt").gte(start);
        }
        if (end != null) {
            criteria.and("createdAt").lte(end);
        }
        long count = mongoService.getMongoPageCount(criteria, Report.class);
        List<Report> reports = mongoService.getList(criteria, page, size, Report.class, Sort.Direction.DESC, "createdAt");
        return new Slice<>(reports.stream().map(ReportInfo::new).collect(Collectors.toList()), page, size, count);
    }

    @Transactional
    public void handleFeedback(String id, String content) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该记录：" + id));
        if (feedback.getUser() != null && StringUtils.isBlank(content)) {
            throw new BadRequestException("有用户记录的必须添加处理意见");
        }
        feedbackRepository.save(feedback.handle());
        if (feedback.getUser() != null) {
            content = "【问题反馈已处理】" + content;
            SendSystemMessageRequest request = new SendSystemMessageRequest(false, feedback.getUser().getId(), content);
            messageService.sendSystemMessage(request);
        }
    }

    @Transactional
    public void handleHobbyApply(String id, String content) {
        HobbyApply hobbyApply = hobbyApplyRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该记录：" + id));
        if (hobbyApply.getUser() != null && StringUtils.isBlank(content)) {
            throw new BadRequestException("有用户记录的必须添加处理意见");
        }
        hobbyApplyRepository.save(hobbyApply.handle());
        if (hobbyApply.getUser() != null) {
            content = "【爱好申请已处理】" + content;
            SendSystemMessageRequest request = new SendSystemMessageRequest(false, hobbyApply.getUser().getId(), content);
            messageService.sendSystemMessage(request);
        }
    }

}
