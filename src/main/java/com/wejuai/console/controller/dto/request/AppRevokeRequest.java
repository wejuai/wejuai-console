package com.wejuai.console.controller.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class AppRevokeRequest {

    @NotBlank(message = "处理原因必填")
    @ApiModelProperty("原因")
    private String reason;

    public String getReason() {
        return reason;
    }
}
