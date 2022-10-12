package com.wejuai.console.service;

import com.wejuai.console.config.WxServiceConfig;
import com.wejuai.console.support.WxServiceClient;
import com.wejuai.console.support.WxTemplateMsg;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.OrdersPageType;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.Withdrawal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ZM.Wang
 */
@Service
public class WxMessageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WxServiceClient wxServiceClient;
    private final WxServiceConfig.Msg wxMsg;
    private final WxServiceConfig.Page page;

    public WxMessageService(WxServiceClient wxServiceClient, WxServiceConfig.Msg wxMsg, WxServiceConfig.Page page) {
        this.wxServiceClient = wxServiceClient;
        this.wxMsg = wxMsg;
        this.page = page;
    }

    public void sendWithdrawalAuditMsg(Withdrawal withdrawal) {
        User recipient = withdrawal.getUser();
        String type = "积分提现";
        String content = recipient.getNickName() + "提现积分: " + withdrawal.getIntegral();
        String result = withdrawal.getStatus() == ApplyStatus.PASS ? "通过" : "驳回";
        String url = page.getWithdrawal() + "?id=" + withdrawal.getId() + "&type=" + OrdersPageType.WITHDRAWAL;
        WxTemplateMsg msg = new WxTemplateMsg().setTemplateId(wxMsg.getAppAudit()).setUrl(url);
        msg.addDataItem("thing3", type).addDataItem("thing5", content)
                .addDataItem("date4", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss")))
                .addDataItem("phrase1", result).addDataItem("thing6", recipient.getNickName());

        sendCommentMsg(recipient, msg);
    }

    private void sendCommentMsg(User user, WxTemplateMsg msg) {
        String openId = user.getAccounts().getWeixinUser().getAppOpenId();
        if (user.getAccounts().getWeixinUser() == null || StringUtils.isBlank(openId)) {
            logger.warn("发送回复模版消息, 该用户未绑定小程序openId: " + user.getId());
            return;
        }
        wxServiceClient.sendWxTemplateMsg(msg.setOpenId(openId));
    }

}
