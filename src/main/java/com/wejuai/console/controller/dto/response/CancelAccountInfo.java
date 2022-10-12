package com.wejuai.console.controller.dto.response;

import com.wejuai.dto.response.UserBaseInfo;
import com.wejuai.entity.mysql.CancelAccount;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class CancelAccountInfo {

    private final UserBaseInfo userInfo;
    private final String createdAt;
    private final String reason;

    public CancelAccountInfo(CancelAccount cancelAccount) {
        this.userInfo = new UserBaseInfo(cancelAccount.getUser());
        this.createdAt = DateFormatUtils.format(cancelAccount.getCreatedAt(), DATE_FORMAT5);
        this.reason = cancelAccount.getReason();
    }

    public UserBaseInfo getUserInfo() {
        return userInfo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getReason() {
        return reason;
    }
}
