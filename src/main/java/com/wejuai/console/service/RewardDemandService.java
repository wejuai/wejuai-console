package com.wejuai.console.service;

import com.endofmaster.commons.util.DateUtil;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.response.AppListInfo;
import com.wejuai.console.controller.dto.response.ApplyCancelRewardRemandInfo;
import com.wejuai.console.controller.dto.response.ApplyCancelRewardRemandListInfo;
import com.wejuai.console.controller.dto.response.RewardSubmissionBaseInfo;
import com.wejuai.console.repository.mongo.SystemMessageRepository;
import com.wejuai.console.repository.mysql.ApplyCancelRewardRemandRepository;
import com.wejuai.console.repository.mysql.RewardDemandRepository;
import com.wejuai.console.repository.mysql.RewardSubmissionRepository;
import com.wejuai.console.repository.mysql.UserRepository;
import com.wejuai.dto.response.RewardDemandInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.SystemMessage;
import com.wejuai.entity.mysql.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ZM.Wang
 */
@Service
public class RewardDemandService {

    private final UserRepository userRepository;
    private final RewardDemandRepository rewardDemandRepository;
    private final RewardSubmissionRepository rewardSubmissionRepository;
    private final SystemMessageRepository systemMessageRepository;
    private final ApplyCancelRewardRemandRepository applyCancelRewardRemandRepository;

    private final MessageService messageService;

    public RewardDemandService(UserRepository userRepository, RewardDemandRepository rewardDemandRepository, RewardSubmissionRepository rewardSubmissionRepository, SystemMessageRepository systemMessageRepository, ApplyCancelRewardRemandRepository applyCancelRewardRemandRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.rewardDemandRepository = rewardDemandRepository;
        this.rewardSubmissionRepository = rewardSubmissionRepository;
        this.systemMessageRepository = systemMessageRepository;
        this.applyCancelRewardRemandRepository = applyCancelRewardRemandRepository;
        this.messageService = messageService;
    }

    public Slice<AppListInfo> rewardDemands(String id, String userId, String hobbyId, Boolean del, RewardDemandStatus status, LocalDate start, LocalDate end, Pageable pageable) {
        if (StringUtils.isNotBlank(id)) {
            RewardDemand rewardDemand = rewardDemandRepository.findById(id).orElse(null);
            List<AppListInfo> articles = rewardDemand == null ? Collections.emptyList() : Collections.singletonList(new AppListInfo(rewardDemand));
            return new Slice<>(articles, 0, 10, rewardDemand == null ? 0 : 1);
        }
        Specification<RewardDemand> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(6);
            if (StringUtils.isNotBlank(hobbyId)) {
                predicates.add(cb.equal(root.get("hobby").get("id"), hobbyId));
            }
            if (del != null) {
                predicates.add(cb.equal(root.get("del"), del));
            }
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (end != null) {
                predicates.add(cb.lessThan(root.get("createdAt"), DateUtil.getAnyDayEnd(end)));
            }
            if (start != null) {
                predicates.add(cb.greaterThan(root.get("createdAt"), DateUtil.getAnyDayStart(start)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<AppListInfo> appListInfos = rewardDemandRepository.findAll(specification, pageable).map(AppListInfo::new);
        return new Slice<>(appListInfos);
    }

    public RewardDemandInfo getRewardDemand(String id) {
        RewardDemand rewardDemand = rewardDemandRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该悬赏: " + id));
        return new RewardDemandInfo(rewardDemand);
    }

    public Page<RewardSubmissionBaseInfo> rewardSubmissions(String rewardDemandId, String userId, Pageable pageable) {
        Specification<RewardSubmission> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(2);
            predicates.add(cb.equal(root.get("rewardDemand").get("id"), rewardDemandId));
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return rewardSubmissionRepository.findAll(specification, pageable).map(RewardSubmissionBaseInfo::new);
    }

    @Transactional
    public void delOrRecoveryRewardDemand(String id, String reason) {
        RewardDemand rewardDemand = rewardDemandRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该悬赏: " + id));
        rewardDemandRepository.save(rewardDemand.setDel(true));
        String text = "【系统删除悬赏】标题：" + rewardDemand.getTitle() + "，原因：" + reason;
        messageService.sendSystemMessage(rewardDemand.getUser().getId(), text, false);
        if (rewardDemand.getStatus() != RewardDemandStatus.END) {
            rewardSubmissionRepository.findByRewardDemand(rewardDemand)
                    .forEach(rewardSubmission -> messageService.sendSystemMessage(rewardSubmission.getUser().getId(), text, false));
        }
    }

    public RewardSubmission getRewardSubmission(String id) {
        return rewardSubmissionRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该回答: " + id));
    }

    public Page<ApplyCancelRewardRemandListInfo> getApplyCancelRewardDemands(ApplyStatus applyStatus, String userId, LocalDate start, LocalDate end, Pageable pageable) {
        Specification<ApplyCancelRewardRemand> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (applyStatus != null) {
                predicates.add(cb.equal(root.get("applyStatus"), applyStatus));
            }
            if (start != null) {
                predicates.add(cb.greaterThan(root.get("createdAt"), start));
            }
            if (end != null) {
                predicates.add(cb.lessThan(root.get("createdAt"), end));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return applyCancelRewardRemandRepository.findAll(specification, pageable).map(ApplyCancelRewardRemandListInfo::new);
    }

    /**
     * 申请返还悬赏全额积分详情
     */
    public ApplyCancelRewardRemandInfo getApplyCancelRewardRemandInfo(String id) {
        ApplyCancelRewardRemand applyCancelRewardRemand = getApplyCancelRewardRemand(id);
        return new ApplyCancelRewardRemandInfo(applyCancelRewardRemand);
    }

    /**
     * 通过返回悬赏全额积分
     */
    @javax.transaction.Transactional
    public void passApplyCancelRewardRemand(String id) {
        ApplyCancelRewardRemand applyCancelRewardRemand = getApplyCancelRewardRemand(id);
        applyCancelRewardRemandRepository.save(applyCancelRewardRemand.pass());
        long integral = applyCancelRewardRemand.getRewardDemand().getIntegral();
        User user = applyCancelRewardRemand.getUser();
        systemMessageRepository.save(new SystemMessage("申请的全额返还积分已通过", user.getId(), false));
        userRepository.save(user.addIntegral(integral).addMsg());
        rewardDemandRepository.save(applyCancelRewardRemand.getRewardDemand().setStatus(RewardDemandStatus.END));
    }

    /**
     * 驳回全额返还悬赏积分的申请
     */
    @javax.transaction.Transactional
    public void unPassApplyCancelRewardRemand(String id, String rejectionReason) {
        ApplyCancelRewardRemand applyCancelRewardRemand = getApplyCancelRewardRemand(id);
        applyCancelRewardRemandRepository.save(applyCancelRewardRemand.notPass(rejectionReason));
        User user = applyCancelRewardRemand.getUser();
        systemMessageRepository.save(new SystemMessage("申请的全额返还积分被驳回，并将状态设置为默认，驳回原因为：" + rejectionReason, user.getId(), false));
        userRepository.save(user.addMsg());
        rewardDemandRepository.save(applyCancelRewardRemand.getRewardDemand().setStatus(RewardDemandStatus.NORMAL));
    }


    private ApplyCancelRewardRemand getApplyCancelRewardRemand(String id) {
        Optional<ApplyCancelRewardRemand> applyCancelRewardRemandOptional = applyCancelRewardRemandRepository.findById(id);
        if (applyCancelRewardRemandOptional.isEmpty()) {
            throw new BadRequestException("没有该返还积分申请");
        }
        return applyCancelRewardRemandOptional.get();
    }

}
