package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.ConsoleOperationRecord;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class ConsoleOperationRecordInfo {
    private final String id;
    private final String createdAt;
    private final String operation;
    private final String ip;
    private final String sessionId;
    private final String method;
    private final String content;

    public ConsoleOperationRecordInfo(ConsoleOperationRecord consoleOperationRecord, String ipInfo) {
        this.id = consoleOperationRecord.getId();
        this.createdAt = DateFormatUtils.format(consoleOperationRecord.getCreatedAt(), DATE_FORMAT5);
        this.sessionId = consoleOperationRecord.getSessionId();
        this.operation = consoleOperationRecord.getOperation();
        this.method = consoleOperationRecord.getMethod();
        this.content = consoleOperationRecord.getContent();
        this.ip = ipInfo;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOperation() {
        return operation;
    }

    public String getIp() {
        return ip;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMethod() {
        return method;
    }

    public String getContent() {
        return content;
    }
}
