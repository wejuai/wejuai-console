package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.Report;
import com.wejuai.entity.mongo.ReportType;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class ReportInfo {

    private final String id;
    private final String createdAt;
    private final ReportType type;
    @ApiModelProperty("举报人")
    private final String userId;
    @ApiModelProperty("被举报人")
    private final String beUserId;
    private final String appType;
    private final String appId;
    private final String reason;

    public ReportInfo(Report report) {
        this.id = report.getId();
        this.createdAt = DateFormatUtils.format(report.getCreatedAt(), DATE_FORMAT5);
        this.type = report.getType();
        this.userId = report.getUserId();
        this.beUserId = report.getBeUserId();
        this.appId = report.getAppId();
        this.appType = report.getAppType();
        this.reason = report.getReason();
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ReportType getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public String getBeUserId() {
        return beUserId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppType() {
        return appType;
    }

    public String getReason() {
        return reason;
    }
}
