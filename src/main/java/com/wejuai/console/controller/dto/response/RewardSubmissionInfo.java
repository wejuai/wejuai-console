package com.wejuai.console.controller.dto.response;

import com.wejuai.dto.response.UserBaseInfo;
import com.wejuai.entity.mysql.RewardSubmission;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class RewardSubmissionInfo {

    private final String id;

    private final String createdAt;

    @ApiModelProperty("基础用户信息")
    private final UserBaseInfo user;

    @ApiModelProperty("简略内容")
    private final String inShort;

    @ApiModelProperty("内容")
    private final String text;

    @ApiModelProperty("是否被选中")
    private final boolean selected;

    @ApiModelProperty("悬赏信息")
    private final RewardDemandBaseInfo rewardDemandInfo;

    public RewardSubmissionInfo(RewardSubmission rewardSubmission) {
        this.id = rewardSubmission.getId();
        this.createdAt = DateFormatUtils.format(rewardSubmission.getCreatedAt(), DATE_FORMAT5);
        this.user = new UserBaseInfo(rewardSubmission.getUser());
        this.inShort = rewardSubmission.getInShort();
        this.selected = rewardSubmission.getSelected();
        this.text = rewardSubmission.getText();
        this.rewardDemandInfo = new RewardDemandBaseInfo(rewardSubmission.getRewardDemand());
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserBaseInfo getUser() {
        return user;
    }

    public String getInShort() {
        return inShort;
    }

    public String getText() {
        return text;
    }

    public boolean getSelected() {
        return selected;
    }

    public RewardDemandBaseInfo getRewardDemandInfo() {
        return rewardDemandInfo;
    }
}
