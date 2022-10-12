package com.wejuai.console.service;

import com.endofmaster.commons.util.DateUtil;
import com.endofmaster.commons.util.StreamUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.wejuai.console.controller.dto.response.ConsoleLoginLogInfo;
import com.wejuai.console.controller.dto.response.ConsoleOperationRecordInfo;
import com.wejuai.console.controller.dto.response.LastLoginInfo;
import com.wejuai.console.repository.mongo.ConsoleLoginLogRepository;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.ConsoleLoginLog;
import com.wejuai.entity.mongo.ConsoleOperationRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.wejuai.console.config.Constant.MAPPER;

/**
 * @author ZM.Wang
 */
@Service
public class SystemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ConsoleLoginLogRepository consoleLoginLogRepository;

    private final MongoService mongoService;
    private final HttpClient httpClient;

    public SystemService(ConsoleLoginLogRepository consoleLoginLogRepository, MongoService mongoService) {
        this.consoleLoginLogRepository = consoleLoginLogRepository;
        this.mongoService = mongoService;
        this.httpClient = HttpClients.createDefault();
    }

    public Slice<ConsoleLoginLogInfo> loginLog(LocalDate start, LocalDate end, long page, long size) {
        Criteria criteria = new Criteria();
        if (start != null & end != null) {
            criteria.and("createdAt").gt(DateUtil.getAnyDayStart(start)).lt(DateUtil.getAnyDayEnd(end));
        }
        long count = mongoService.getMongoPageCount(criteria, ConsoleLoginLog.class);
        List<ConsoleLoginLogInfo> data = mongoService.getList(criteria, page, size, ConsoleLoginLog.class, Sort.Direction.DESC, "createdAt")
                .stream().map(consoleLoginLog -> {
                    String ipInfo = consoleLoginLog.getIp() + "  (" + getIpInfo(consoleLoginLog.getIp()) + ")";
                    return new ConsoleLoginLogInfo(consoleLoginLog, ipInfo);
                }).collect(Collectors.toList());
        return new Slice<>(data, page, size, count);
    }

    public Slice<ConsoleOperationRecordInfo> operationRecords(LocalDate start, LocalDate end, String method, String operation, long page, long size) {
        Criteria criteria = new Criteria();
        if (start != null & end != null) {
            criteria.and("createdAt").gt(DateUtil.getAnyDayStart(start)).lt(DateUtil.getAnyDayEnd(end));
        }
        if (StringUtils.isNotBlank(method)) {
            criteria.and("method").is(method);
        }
        if (StringUtils.isNotBlank(operation)) {
            criteria.and("operation").regex(operation);
        }
        long count = mongoService.getMongoPageCount(criteria, ConsoleOperationRecord.class);
        List<ConsoleOperationRecordInfo> data = mongoService.getList(criteria, page, size, ConsoleOperationRecord.class, Sort.Direction.DESC, "createdAt")
                .stream().map(consoleOperationRecord -> {
                    String ipInfo = consoleOperationRecord.getIp() + "  (" + getIpInfo(consoleOperationRecord.getIp()) + ")";
                    return new ConsoleOperationRecordInfo(consoleOperationRecord, ipInfo);
                }).collect(Collectors.toList());
        return new Slice<>(data, page, size, count);
    }

    public LastLoginInfo getLastLoginInfo() {
        ConsoleLoginLog consoleLoginLog = consoleLoginLogRepository.findTop2ByOrderByCreatedAtDesc().get(1);
        String address = consoleLoginLog.getIp() + "   (" + getIpInfo(consoleLoginLog.getIp()) + ")";
        return new LastLoginInfo(consoleLoginLog.getCreatedAt(), address);
    }

    private String getIpInfo(String ip) {
        if (StringUtils.isBlank(ip) || StringUtils.equals("127.0.0.1", ip)) {
            return "";
        }
        try {
            HttpGet get = new HttpGet("https://opendata.baidu.com/api.php?ie=utf8&oe=utf8&format=json&resource_id=6006&tn=baidu&query=" + ip);
            HttpResponse response = httpClient.execute(get);
            String json = StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            logger.debug("ip查询结果: " + json);
            JsonNode jsonNode = MAPPER.readTree(json);
            return jsonNode.get("data").get(0).get("location").asText();
        } catch (Exception e) {
            logger.error("查询ip错误", e);
            return "";
        }
    }

}
