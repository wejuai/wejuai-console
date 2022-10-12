package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.ConsoleLoginLog;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class ConsoleLoginLogInfo {

    private final String id;
    private final String createdAt;
    private final String ip;
    private final String sessionId;

    public ConsoleLoginLogInfo(ConsoleLoginLog consoleLoginLog, String ipInfo) {
        this.id = consoleLoginLog.getId();
        this.createdAt = DateFormatUtils.format(consoleLoginLog.getCreatedAt(), DATE_FORMAT5);
        this.sessionId = consoleLoginLog.getSessionId();
        this.ip = ipInfo;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getIp() {
        return ip;
    }

    public String getSessionId() {
        return sessionId;
    }
}
