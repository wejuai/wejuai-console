package com.wejuai.console.controller;

import com.wejuai.console.controller.dto.response.HobbyHotInfo;
import com.wejuai.console.controller.dto.response.SChartInfo;
import com.wejuai.console.service.StatisticsService;
import com.wejuai.entity.mongo.statistics.ChargeStatistics;
import com.wejuai.entity.mongo.statistics.ChargeStatisticsByDay;
import com.wejuai.entity.mongo.statistics.HobbyTotalHot;
import com.wejuai.entity.mongo.statistics.OrdersStatistics;
import com.wejuai.entity.mongo.statistics.OrdersStatisticsByDay;
import com.wejuai.entity.mongo.statistics.UserStatistics;
import com.wejuai.entity.mongo.statistics.UserStatisticsByDay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
@Api(tags = "各类统计结果")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @ApiOperation("订单统计")
    @GetMapping("/orders")
    public OrdersStatistics getOrdersStatistics() {
        return statisticsService.getOrders();
    }

    @ApiModelProperty("财务统计")
    @GetMapping("/charge")
    public ChargeStatistics getChargeStatistics() {
        return statisticsService.getCharge();
    }

    @ApiModelProperty("用户统计")
    @GetMapping("/user")
    public UserStatistics getUserStatistics() {
        return statisticsService.getUser();
    }

    @ApiModelProperty("爱好统计")
    @GetMapping("/hobby")
    public HobbyTotalHot getHobbyStatistics() {
        return statisticsService.getHobby();
    }

    @ApiOperation("按天统计charge折线图")
    @GetMapping("/charge/day")
    public SChartInfo chargeStatisticsByDays(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end) {
        if (start == null || end == null) {
            end = LocalDate.now();
            start = end.plusDays(-8);
        } else {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        return statisticsService.chargeStatisticsByDays(start, end);
    }

    @ApiOperation("按天统计charge分页表")
    @GetMapping("/charge/day/page")
    public Page<ChargeStatisticsByDay> chargeStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        if (start != null && end != null) {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return statisticsService.chargeStatistics(start, end, pageable);
    }

    @ApiOperation("按天统计订单折线图")
    @GetMapping("/orders/day")
    public SChartInfo findByCreatedAtBetween(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end) {
        if (start == null || end == null) {
            end = LocalDate.now();
            start = end.plusDays(-8);
        } else {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        return statisticsService.ordersStatisticsByDays(start, end);
    }

    @ApiOperation("按天统计orders分页表")
    @GetMapping("/orders/day/page")
    public Page<OrdersStatisticsByDay> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        if (start != null && end != null) {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return statisticsService.ordersStatistics(start, end, pageable);
    }

    @ApiOperation("按天统计用户折线图")
    @GetMapping("/user/day")
    public SChartInfo userStatisticsByDay(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end) {
        if (start == null || end == null) {
            end = LocalDate.now();
            start = end.plusDays(-8);
        } else {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        return statisticsService.userStatisticsByDay(start, end);
    }

    @ApiOperation("按天统计user分页表")
    @GetMapping("/user/day/page")
    public Page<UserStatisticsByDay> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end,
                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        if (start != null && end != null) {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return statisticsService.userStatistics(start, end, pageable);
    }


    @ApiOperation("按天统计用户折线图")
    @GetMapping("/hobby/day")
    public SChartInfo hobbyStatisticsByDay(@RequestParam(required = false, defaultValue = "") String hobbyId,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end) {
        if (start == null || end == null) {
            end = LocalDate.now();
            start = end.plusDays(-8);
        } else {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        return statisticsService.hobbyStatisticsByDay(hobbyId, start, end);
    }

    @ApiOperation("按天统计user分页表")
    @GetMapping("/hobby/day/page")
    public Page<HobbyHotInfo> hobbyStatistics(@RequestParam(required = false, defaultValue = "") String hobbyId,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate start,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate end,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        if (start != null && end != null) {
            start = start.plusDays(-1);
            end = end.plusDays(1);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return statisticsService.hobbyStatistics(hobbyId, start, end, pageable);
    }
}
