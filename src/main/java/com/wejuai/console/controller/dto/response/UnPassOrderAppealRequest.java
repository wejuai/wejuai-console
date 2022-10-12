package com.wejuai.console.controller.dto.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class UnPassOrderAppealRequest {

    @ApiModelProperty("驳回原因")
    private String rejectionReason;

    public String getRejectionReason() {
        return rejectionReason;
    }
}
