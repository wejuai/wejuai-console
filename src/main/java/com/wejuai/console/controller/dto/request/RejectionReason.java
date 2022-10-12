package com.wejuai.console.controller.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class RejectionReason {

    @ApiModelProperty("驳回理由")
    @NotBlank(message = "驳回理由必填")
    private String rejectionReason;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public RejectionReason setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
        return this;
    }
}
