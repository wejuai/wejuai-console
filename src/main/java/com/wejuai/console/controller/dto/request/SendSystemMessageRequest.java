package com.wejuai.console.controller.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class SendSystemMessageRequest {

    @ApiModelProperty("是否群发")
    private boolean groupPush;

    @ApiModelProperty("非群发的用户id")
    private String userId;

    @NotBlank
    @ApiModelProperty("内容")
    private String text;

    public SendSystemMessageRequest(boolean groupPush, String userId, @NotBlank String text) {
        this.groupPush = groupPush;
        this.userId = userId;
        this.text = text;
    }

    SendSystemMessageRequest(){}

    public boolean getGroupPush() {
        return groupPush;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }
}
