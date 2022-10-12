package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.request.SendSystemMessageRequest;
import com.wejuai.console.service.MessageService;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.SystemMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation("发送系统消息")
    @PostMapping("/system/send")
    public void sendSystemMessage(@RequestBody @Valid SendSystemMessageRequest request) {
        messageService.sendSystemMessage(request);
    }

    @ApiOperation("系统消息列表")
    @GetMapping("/system")
    public Slice<SystemMessage> getSystemMessages(@RequestParam(required = false, defaultValue = "") String userId,
                                                  @RequestParam(required = false, defaultValue = "") Boolean groupPush,
                                                  @RequestParam(required = false, defaultValue = "")
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                  @RequestParam(required = false, defaultValue = "")
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                  @RequestParam(required = false, defaultValue = "0") long page,
                                                  @RequestParam(required = false, defaultValue = "10") long size) {
        return messageService.getSystemMessages(userId, groupPush, start, end, page, size);
    }
}
