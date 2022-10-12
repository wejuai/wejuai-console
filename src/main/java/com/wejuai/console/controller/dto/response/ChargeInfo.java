package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.trade.Charge;
import com.wejuai.entity.mongo.trade.TradeStatus;
import com.wejuai.entity.mysql.ChannelType;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Map;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class ChargeInfo {

    private final String id;
    private final String userId;
    @ApiModelProperty("金额分")
    private final long amount;
    @ApiModelProperty("通道类型")
    private final ChannelType channelType;
    @ApiModelProperty("通知地址")
    private final String notifyUrl;
    @ApiModelProperty("返回地址")
    private final String returnUrl;
    @ApiModelProperty("创建时间")
    private final String startedAt;
    @ApiModelProperty("状态")
    private final TradeStatus status;
    @ApiModelProperty("完成时间")
    private final String completedAt;
    @ApiModelProperty("通道订单号")
    private final String chanTradeNo;
    @ApiModelProperty("错误信息")
    private final String errorMessage;
    @ApiModelProperty("支付凭证")
    private final Map<String, String> credentials;
    @ApiModelProperty("后续需求参数")
    private final Map<String, String> resultParams;
    @ApiModelProperty("订单名")
    private final String subject;

    public ChargeInfo(Charge charge) {
        this.id = charge.getId();
        this.userId = charge.getUserId();
        this.amount = charge.getAmount();
        this.channelType = charge.getChannelType();
        this.notifyUrl = charge.getNotifyUrl();
        this.returnUrl = charge.getReturnUrl();
        this.startedAt = DateFormatUtils.format(charge.getStartedAt(), DATE_FORMAT5);
        this.status = charge.getStatus();
        this.credentials = charge.getCredentials();
        this.resultParams = charge.getResultParams();
        this.subject = charge.getSubject();
        this.completedAt = charge.getCompletedAt() != null ? DateFormatUtils.format(charge.getCompletedAt(), DATE_FORMAT5) : null;
        this.chanTradeNo = charge.getChanTradeNo();
        this.errorMessage = charge.getErrorMessage();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public long getAmount() {
        return amount;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public String getChanTradeNo() {
        return chanTradeNo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public Map<String, String> getResultParams() {
        return resultParams;
    }

    public String getSubject() {
        return subject;
    }
}
