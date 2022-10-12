package com.wejuai.console.controller.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.wejuai.dto.response.UserBaseInfo;
import com.wejuai.entity.mysql.ApplyCancelRewardRemand;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.RewardDemand;
import com.wejuai.entity.mysql.RewardDemandStatus;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
public class ApplyCancelRewardRemandInfo {

    private String id;
    @ApiModelProperty("请求理由")
    private String reason;
    @ApiModelProperty("用户基础信息")
    private UserBaseInfo user;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String text;
    @ApiModelProperty("积分")
    private long integral;
    @ApiModelProperty("截止日期")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate deadline;
    @ApiModelProperty("悬赏状态")
    private RewardDemandStatus status;
    @ApiModelProperty("是否延期过")
    private boolean extension;
    @ApiModelProperty("总答题数量")
    private long rewardSubmissionCount;
    @ApiModelProperty("申请状态")
    private ApplyStatus applyStatus;
    @ApiModelProperty("驳回原因")
    private String rejectionReason;

    public ApplyCancelRewardRemandInfo(ApplyCancelRewardRemand applyCancelRewardRemand) {
        this.id = applyCancelRewardRemand.getId();
        this.user = new UserBaseInfo(applyCancelRewardRemand.getUser());
        this.reason = applyCancelRewardRemand.getReason();
        this.rejectionReason = applyCancelRewardRemand.getRejectionReason();
        this.applyStatus = applyCancelRewardRemand.getStatus();
        RewardDemand rewardDemand = applyCancelRewardRemand.getRewardDemand();
        if (rewardDemand != null) {
            this.integral = rewardDemand.getIntegral();
            this.title = rewardDemand.getTitle();
            this.text = rewardDemand.getText();
            this.status = rewardDemand.getStatus();
            this.deadline = rewardDemand.getDeadline();
            this.extension = rewardDemand.getExtension();
            this.rewardSubmissionCount = rewardDemand.getRewardSubmissionCount();
        }
    }

    ApplyCancelRewardRemandInfo() {
    }

    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public UserBaseInfo getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getIntegral() {
        return integral;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public RewardDemandStatus getStatus() {
        return status;
    }

    public ApplyStatus getApplyStatus() {
        return applyStatus;
    }

    public boolean getExtension() {
        return extension;
    }

    public long getRewardSubmissionCount() {
        return rewardSubmissionCount;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
