package com.wejuai.console.controller;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.request.UpdateOneValueRequest;
import com.wejuai.console.controller.dto.request.UpdateUserInfoRequest;
import com.wejuai.console.controller.dto.response.CancelAccountInfo;
import com.wejuai.console.controller.dto.response.UserHobbyInfo;
import com.wejuai.console.controller.dto.response.UserInfo;
import com.wejuai.console.controller.dto.response.UserListInfo;
import com.wejuai.console.service.MessageService;
import com.wejuai.console.service.UserService;
import com.wejuai.entity.mysql.OauthType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

import static com.wejuai.dto.Constant.DATE_FORMAT6;


/**
 * @author ZM.Wang
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    public UserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @ApiOperation("用户列表")
    @GetMapping
    public Page<UserListInfo> getUsers(@RequestParam(required = false, defaultValue = "") String id,
                                       @RequestParam(required = false, defaultValue = "") String phone,
                                       @RequestParam(required = false, defaultValue = "") String email,
                                       @RequestParam(required = false, defaultValue = "") String nickname,
                                       @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = DATE_FORMAT6) LocalDate start,
                                       @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = DATE_FORMAT6) LocalDate end,
                                       @RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return userService.getUsers(id, phone, email, nickname, start, end, pageable);
    }

    @ApiOperation("用户详情")
    @GetMapping("/{id}")
    public UserInfo getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @ApiOperation("用户爱好信息")
    @GetMapping("/{id}/hobby")
    public UserHobbyInfo getUserHobby(@PathVariable String id) {
        return userService.getUserHobby(id);
    }

    @ApiOperation("添加爱好")
    @PutMapping("/{id}/hobby/{hobbyId}/add")
    public void addHobby(@PathVariable String id, @PathVariable String hobbyId) {
        userService.addHobby(id, hobbyId);
    }

    @ApiOperation("减少爱好")
    @PutMapping("/{id}/hobby/{hobbyId}/sub")
    public void subHobby(@PathVariable String id, @PathVariable String hobbyId) {
        userService.subHobby(id, hobbyId);
    }

    @ApiOperation("重置第三方账号")
    @PutMapping("/{id}/resetOtherUser/{type}")
    public void resetOtherUser(@PathVariable String id, @PathVariable OauthType type) {
        userService.resetOtherUser(id, type);
    }

    @ApiOperation("修改手机号，请求体中value是phone")
    @PutMapping("/{id}/phone")
    public void updatePhone(@PathVariable String id, @RequestBody @Valid UpdateOneValueRequest request) {
        userService.updatePhone(id, request.getValue());
    }

    @ApiOperation("封禁或者解封用户")
    @PutMapping("/{userId}/ban")
    public void ban(@PathVariable String userId, @RequestParam boolean ban) {
        userService.ban(userId, ban);
    }

    @ApiOperation("重新计算用户消息数")
    @PutMapping("/{userId}/recalculateMsgNum")
    public void recalculateUserMsgNum(@PathVariable String userId) {
        messageService.recalculateUserMsgNum(userId);
    }

    @ApiOperation("获取申请注销账号列表")
    @GetMapping("/cancelAccounts")
    public Page<CancelAccountInfo> getCancelAccounts(@RequestParam(required = false, defaultValue = "") String userId,
                                                     @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = DATE_FORMAT6) LocalDate start,
                                                     @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = DATE_FORMAT6) LocalDate end,
                                                     @RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return userService.getCancelAccounts(userId, start, end, pageable);
    }

    @ApiOperation("注销账户")
    @DeleteMapping("/cancelAccount/{userId}")
    public void cancelAccount(@PathVariable String userId, @RequestParam String password) {
        if (!StringUtils.equals("jkn23#@$jni23", password)) {
            throw new BadRequestException("密码错误");
        }
        userService.cancelAccount(userId);
    }

    @ApiOperation("修改系统账号的头像")
    @PutMapping("/system/avatar")
    public void updateSystemUserAvatar(@RequestBody UpdateOneValueRequest request) {
        userService.updateSystemUserAvatar(request.getValue());
    }

    @ApiOperation("修改系统账号信息")
    @PutMapping("/system")
    public void updateSystemUser(@RequestBody UpdateUserInfoRequest request) {
        userService.updateSystemUser(request);
    }
}
