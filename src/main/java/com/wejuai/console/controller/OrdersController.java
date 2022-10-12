package com.wejuai.console.controller;

import com.endofmaster.commons.util.DateUtil;
import com.wejuai.console.controller.dto.response.UnPassOrderAppealRequest;
import com.wejuai.console.service.OrdersService;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.response.OrderAppealInfo;
import com.wejuai.dto.response.OrdersInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.OrdersPageType;
import com.wejuai.entity.mysql.OrdersType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author ZM.Wang
 */
@Api(tags = "订单相关, 主要为积分相关")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public OrdersController(OrdersService ordersService, WejuaiCoreClient wejuaiCoreClient) {
        this.ordersService = ordersService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("订单列表接口")
    @GetMapping
    public Slice<OrdersInfo> getOrders(@RequestParam(required = false, defaultValue = "") String userId,
                                       @RequestParam(required = false, defaultValue = "") OrdersType type,
                                       @RequestParam(required = false, defaultValue = "") Boolean income,
                                       @RequestParam(required = false, defaultValue = "true")
                                       @ApiParam("是否不展示0积分单,默认true") boolean notZero,
                                       @RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "10") int size,
                                       @RequestParam(required = false, defaultValue = "")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                       @RequestParam(required = false, defaultValue = "")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return wejuaiCoreClient.getOrders(userId, type, income, notZero,
                start == null ? null : DateUtil.localDate2Long(LocalDateTime.of(start, LocalTime.MIN)),
                end == null ? null : DateUtil.localDate2Long(LocalDateTime.of(end, LocalTime.MIN)),
                page, size);
    }

    @ApiOperation("订单申诉列表")
    @GetMapping("/orderAppeal")
    public Slice<OrderAppealInfo> getOrderAppeals(@RequestParam(required = false, defaultValue = "") String userId,
                                                  @RequestParam(required = false, defaultValue = "") String start,//yyyy-MM-dd
                                                  @RequestParam(required = false, defaultValue = "") String end,
                                                  @RequestParam(required = false, defaultValue = "") ApplyStatus status,
                                                  @RequestParam(required = false, defaultValue = "") OrdersPageType type,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getOrderAppeals(userId, start, end, status, type, page, size);
    }

    @ApiOperation("已处理订单申诉")
    @PutMapping("/orderAppeal/{id}/pass")
    public void passOrderAppeal(@PathVariable String id) {
        ordersService.passOrderAppeal(id);
    }

    @ApiOperation("驳回订单申诉")
    @PutMapping("/orderAppeal/{id}/unPass")
    public void unPassOrderAppeal(@PathVariable String id, @RequestBody UnPassOrderAppealRequest request) {
        ordersService.unPassOrderAppeal(id, request.getRejectionReason());
    }

    @ApiOperation("直接给用户添加积分")
    @PostMapping("/integral/{integral}/add")
    public void addIntegral(@PathVariable long integral, @RequestParam String userId) {
        ordersService.addIntegral(userId, integral);
    }

    @ApiOperation("直接给用户减少积分")
    @PostMapping("/integral/{integral}/sub")
    public void subIntegral(@PathVariable long integral, @RequestParam String userId) {
        ordersService.subIntegral(userId, integral);
    }
}
