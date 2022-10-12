package com.wejuai.console.controller.dto.response;

import com.wejuai.dto.response.UserBaseInfo;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.Withdrawal;
import com.wejuai.entity.mysql.WithdrawalType;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT;

/**
 * @author ZM.Wang
 */
public class WithdrawalListInfo {

    private String id;
    private String createdAt;
    @ApiModelProperty("积分")
    private long integral;
    @ApiModelProperty("状态")
    private ApplyStatus status;
    @ApiModelProperty("用户基础信息")
    private UserBaseInfo user;
    @ApiModelProperty("审核时间")
    private String auditedAt;
    @ApiModelProperty("提现通道")
    private WithdrawalType channelType;
    @ApiModelProperty("转账状态")
    private boolean tradeStatus;

    public WithdrawalListInfo(Withdrawal withdrawal) {
        this.id = withdrawal.getId();
        this.createdAt = DateFormatUtils.format(withdrawal.getCreatedAt(), DATE_FORMAT);
        this.integral = withdrawal.getIntegral();
        this.status = withdrawal.getStatus();
        this.user = new UserBaseInfo(withdrawal.getUser());
        this.auditedAt = withdrawal.getAuditedAt() == null ? null : DateFormatUtils.format(withdrawal.getAuditedAt(), DATE_FORMAT);
        this.channelType = withdrawal.getChannelType();
        this.tradeStatus = withdrawal.getTradeStatus();
    }

    WithdrawalListInfo() {
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

    public ApplyStatus getStatus() {
        return status;
    }

    public UserBaseInfo getUser() {
        return user;
    }

    public String getAuditedAt() {
        return auditedAt;
    }

    public WithdrawalType getChannelType() {
        return channelType;
    }

    public boolean getTradeStatus() {
        return tradeStatus;
    }
}
