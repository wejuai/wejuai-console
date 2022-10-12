package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mysql.RewardDemand;
import com.wejuai.entity.mysql.RewardDemandStatus;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class RewardDemandBaseInfo {

    private final String id;
    @ApiModelProperty("创建时间，yyyy-MM-dd HH:mm:ss")
    private final String createdAt;
    @ApiModelProperty("用户id")
    private final String userId;
    @ApiModelProperty("爱好id")
    private final String hobbyId;
    @ApiModelProperty("标题")
    private final String title;
    @ApiModelProperty("是否删除")
    private final boolean del;
    @ApiModelProperty("积分")
    private final long integral;
    @ApiModelProperty("截止日期")
    private final String deadline;
    @ApiModelProperty("状态")
    private final RewardDemandStatus status;
    @ApiModelProperty("是否延期过")
    private final boolean extension;
    @ApiModelProperty("评分限制")
    private final double scoreLimit;
    @ApiModelProperty("总答题数量")
    private final long rewardSubmissionCount;

    public RewardDemandBaseInfo(RewardDemand rewardDemand) {
        this.id = rewardDemand.getId();
        this.createdAt = DateFormatUtils.format(rewardDemand.getCreatedAt(), DATE_FORMAT5);
        this.userId = rewardDemand.getUser().getId();
        this.hobbyId = rewardDemand.getHobby().getId();
        this.title = rewardDemand.getTitle();
        this.del = rewardDemand.getDel();
        this.integral = rewardDemand.getIntegral();
        this.deadline = rewardDemand.getDeadline().toString();
        this.status = rewardDemand.getStatus();
        this.extension = rewardDemand.getExtension();
        this.scoreLimit = rewardDemand.getScoreLimit();
        this.rewardSubmissionCount = rewardDemand.getRewardSubmissionCount();
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getHobbyId() {
        return hobbyId;
    }

    public String getTitle() {
        return title;
    }

    public boolean getDel() {
        return del;
    }

    public long getIntegral() {
        return integral;
    }

    public String getDeadline() {
        return deadline;
    }

    public RewardDemandStatus getStatus() {
        return status;
    }

    public boolean getExtension() {
        return extension;
    }

    public double getScoreLimit() {
        return scoreLimit;
    }

    public long getRewardSubmissionCount() {
        return rewardSubmissionCount;
    }
}
