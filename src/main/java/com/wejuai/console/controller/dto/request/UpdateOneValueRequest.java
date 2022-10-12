package com.wejuai.console.controller.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class UpdateOneValueRequest {

    @NotBlank
    @ApiModelProperty("接口对应的值")
    private String value;

    public String getValue() {
        return value;
    }
}
