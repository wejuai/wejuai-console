package com.wejuai.console.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZM.Wang
 */
@Api(tags = "检查")
@RestController
@RequestMapping("/api/check")
public class CheckController {

    @ApiOperation("检查登录状态")
    @GetMapping
    public void checkLogin() {
    }
}
