package com.wejuai.console.controller;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.request.LoginRequest;
import com.wejuai.console.repository.mongo.ConsoleLoginLogRepository;
import com.wejuai.entity.mongo.ConsoleLoginLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.wejuai.console.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "账号")
@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final ConsoleLoginLogRepository consoleLoginLogRepository;

    public AccountsController(ConsoleLoginLogRepository consoleLoginLogRepository) {
        this.consoleLoginLogRepository = consoleLoginLogRepository;
    }

    @ApiOperation("普通邮箱登录")
    @PostMapping("/login")
    public void login(@RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                      HttpServletRequest servletRequest,
                      @RequestBody @Valid LoginRequest request, HttpSession session) {
        if (!StringUtils.equals("wejuai", request.getUsername()) || !StringUtils.equals("wejuai#$%23jafewg", request.getPassword())) {
            throw new BadRequestException("账号或密码错误");
        }
        String ip = (ips == null || ips.size() < 1) ? servletRequest.getRemoteHost() : ips.get(0);
        consoleLoginLogRepository.save(new ConsoleLoginLog(ip, session.getId()));
        session.setAttribute(SESSION_LOGIN, "wejuai");
    }

    @ApiOperation("退出")
    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_LOGIN);
    }
}
