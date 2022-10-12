package com.wejuai.console.controller.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class UpdateHobbyTabsRequest {

    @NotBlank
    @ApiModelProperty("添加爱好标签是标签内容，删除标签是id")
    private String tab;

    public String getTab() {
        return tab;
    }
}
