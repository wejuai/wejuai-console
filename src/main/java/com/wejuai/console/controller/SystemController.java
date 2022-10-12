package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.response.ConsoleLoginLogInfo;
import com.wejuai.console.controller.dto.response.ConsoleOperationRecordInfo;
import com.wejuai.console.controller.dto.response.LastLoginInfo;
import com.wejuai.console.service.SystemService;
import com.wejuai.dto.response.Slice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
@Api(tags = "系统管理")
@RestController
@RequestMapping("/api/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @ApiOperation("console登录日志")
    @GetMapping("/loginLog")
    public Slice<ConsoleLoginLogInfo> loginLog(@RequestParam(required = false, defaultValue = "")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                               @RequestParam(required = false, defaultValue = "")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                               @RequestParam(required = false, defaultValue = "0") long page,
                                               @RequestParam(required = false, defaultValue = "10") long size) {
        return systemService.loginLog(start, end, page, size);
    }

    @ApiOperation("操作记录")
    @GetMapping("/operations")
    public Slice<ConsoleOperationRecordInfo> operationRecords(@RequestParam(required = false, defaultValue = "")
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                              @RequestParam(required = false, defaultValue = "")
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                              @RequestParam(required = false, defaultValue = "") String method,
                                                              @RequestParam(required = false, defaultValue = "") String operation,
                                                              @RequestParam(required = false, defaultValue = "0") long page,
                                                              @RequestParam(required = false, defaultValue = "10") long size) {
        return systemService.operationRecords(start, end, method, operation, page, size);
    }

    @ApiOperation("获取最后一次登录信息")
    @GetMapping("/lastLoginInfo")
    public LastLoginInfo getLastLoginInfo() {
        return systemService.getLastLoginInfo();
    }
}
