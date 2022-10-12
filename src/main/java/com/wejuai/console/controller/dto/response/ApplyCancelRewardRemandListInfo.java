package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mysql.ApplyCancelRewardRemand;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.RewardDemand;
import com.wejuai.entity.mysql.RewardDemandStatus;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT;
import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class ApplyCancelRewardRemandListInfo {

    private String id;
    @ApiModelProperty("创建时间")
    private String createdAt;
    @ApiModelProperty("积分")
    private long integral;
    @ApiModelProperty("悬赏状态")
    private RewardDemandStatus status;
    @ApiModelProperty("申请状态")
    private ApplyStatus applyStatus;
    @ApiModelProperty("申请原因")
    private String reason;
    @ApiModelProperty("驳回原因")
    private String rejectionReason;
    @ApiModelProperty("完成时间")
    private String completedAt;
    @ApiModelProperty("悬赏id")
    private String rewardRemandId;

    public ApplyCancelRewardRemandListInfo(ApplyCancelRewardRemand apply) {
        this.id = apply.getId();
        this.createdAt = DateFormatUtils.format(apply.getCreatedAt(), DATE_FORMAT);
        this.applyStatus = apply.getStatus();
        this.reason = apply.getReason();
        this.rejectionReason = apply.getRejectionReason();
        this.completedAt = apply.getCompletedAt() == null ? "" : DateFormatUtils.format(apply.getCompletedAt(), DATE_FORMAT5);
        RewardDemand rewardDemand = apply.getRewardDemand();
        if (rewardDemand != null) {
            this.rewardRemandId = rewardDemand.getId();
            this.integral = rewardDemand.getIntegral();
            this.status = rewardDemand.getStatus();
        }
    }

    ApplyCancelRewardRemandListInfo() {
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getIntegral() {
        return integral;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public RewardDemandStatus getStatus() {
        return status;
    }

    public ApplyStatus getApplyStatus() {
        return applyStatus;
    }

    public String getReason() {
        return reason;
    }

    public String getRewardRemandId() {
        return rewardRemandId;
    }
}
