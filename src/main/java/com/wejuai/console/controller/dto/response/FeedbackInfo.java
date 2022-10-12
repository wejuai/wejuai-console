package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mysql.Feedback;
import com.wejuai.entity.mysql.HobbyApply;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT;

/**
 * @author ZM.Wang
 */
public class FeedbackInfo {

    private String id;

    @ApiModelProperty("提交时间")
    private String createdAt;

    @ApiModelProperty("提交内容")
    private String text;

    @ApiModelProperty("联系方式")
    private String contact;

    @ApiModelProperty("是否已经处理")
    private boolean handle;

    private String userId;

    public FeedbackInfo(HobbyApply hobbyApply) {
        this.id = hobbyApply.getId();
        this.createdAt = DateFormatUtils.format(hobbyApply.getCreatedAt(), DATE_FORMAT);
        this.text = hobbyApply.getText();
        this.contact = hobbyApply.getContact();
        this.handle = hobbyApply.getHandle();
        if (hobbyApply.getUser() != null) {
            this.userId = hobbyApply.getUser().getId();
        }
    }

    public FeedbackInfo(Feedback feedback) {
        this.id = feedback.getId();
        this.createdAt = DateFormatUtils.format(feedback.getCreatedAt(), DATE_FORMAT);
        this.text = feedback.getText();
        this.contact = feedback.getContact();
        this.handle = feedback.getHandle();
        if (feedback.getUser() != null) {
            this.userId = feedback.getUser().getId();
        }
    }

    FeedbackInfo() {
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public String getContact() {
        return contact;
    }

    public boolean getHandle() {
        return handle;
    }

    public String getUserId() {
        return userId;
    }
}
