package com.wejuai.console.controller.dto.response;

import com.wejuai.dto.response.UserBaseInfo;
import com.wejuai.entity.mysql.RewardSubmission;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class RewardSubmissionBaseInfo {

    private String id;

    private String createdAt;

    @ApiModelProperty("基础用户信息")
    private UserBaseInfo user;

    @ApiModelProperty("简略内容")
    private String inShort;

    @ApiModelProperty("是否被选中")
    private boolean selected;

    public RewardSubmissionBaseInfo(RewardSubmission rewardSubmission) {
        this.id = rewardSubmission.getId();
        this.createdAt = DateFormatUtils.format(rewardSubmission.getCreatedAt(), DATE_FORMAT5);
        this.user = new UserBaseInfo(rewardSubmission.getUser());
        this.inShort = rewardSubmission.getInShort();
        this.selected = rewardSubmission.getSelected();
    }

    RewardSubmissionBaseInfo() {
    }

    public UserBaseInfo getUser() {
        return user;
    }

    public String getInShort() {
        return inShort;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean getSelected() {
        return selected;
    }
}
