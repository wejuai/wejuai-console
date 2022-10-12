package com.wejuai.console.controller;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.service.ChargeService;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.request.WithdrawalTradeRequest;
import com.wejuai.dto.response.ChargeListInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.dto.response.UserIntegralInfo;
import com.wejuai.dto.response.WithdrawalInfo;
import com.wejuai.entity.mongo.MerchantTransfer;
import com.wejuai.entity.mongo.trade.TradeStatus;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.ChannelQueryType;
import com.wejuai.entity.mysql.WithdrawalType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author ZM.Wang
 */
@Api(tags = "支付相关, 和实际金钱相关")
@RestController
@RequestMapping("/api/charge")
public class ChargeController {

    private final ChargeService chargeService;

    private final WejuaiCoreClient wejuaiCoreClient;

    public ChargeController(ChargeService chargeService, WejuaiCoreClient wejuaiCoreClient) {
        this.chargeService = chargeService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("订单列表")
    @GetMapping
    public Slice<ChargeListInfo> getChargeInfos(@RequestParam(required = false, defaultValue = "") TradeStatus status,
                                                @RequestParam(required = false, defaultValue = "ALL") ChannelQueryType channelType,
                                                @RequestParam(required = false, defaultValue = "") String userId,
                                                @RequestParam(required = false, defaultValue = "") String id,
                                                @RequestParam(required = false, defaultValue = "") Long start,
                                                @RequestParam(required = false, defaultValue = "") Long end,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        return chargeService.getCharges(id, userId, status, channelType, start, end, page, size);
    }

    @ApiOperation("提现申请列表")
    @GetMapping("/withdrawal")
    public Slice<WithdrawalInfo> getWithdrawals(@RequestParam(required = false, defaultValue = "") String userId,
                                                @RequestParam(required = false, defaultValue = "") ApplyStatus status,
                                                @RequestParam(required = false, defaultValue = "") String id,
                                                @RequestParam(required = false, defaultValue = "") WithdrawalType channelType,
                                                @RequestParam(required = false, defaultValue = "") Long start,
                                                @RequestParam(required = false, defaultValue = "") Long end,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getWithdrawals(id, userId, status, channelType, start, end, page, size);
    }

    @ApiOperation("提现记录详情")
    @GetMapping("/withdrawal/{id}")
    public WithdrawalInfo getWithdrawalInfo(@PathVariable String id) {
        return chargeService.getWithdrawalInfo(id);
    }

    @ApiOperation("同步用户积分")
    @PutMapping("/withdrawal/{userId}/sync")
    public UserIntegralInfo syncUserIntegral(@PathVariable String userId) {
        return chargeService.syncUserIntegral(userId);
    }

    @ApiOperation("通过提现")
    @PostMapping("/withdrawal/{id}/pass")
    public void passWithdrawal(@PathVariable String id) {
        chargeService.passWithdrawal(id);
    }

    @ApiOperation("驳回提现")
    @PostMapping("/withdrawal/{id}/unPass")
    public void unPassWithdrawal(@PathVariable String id, @RequestParam String rejectionReason) {
        chargeService.unPassWithdrawal(id, rejectionReason);
    }

    @ApiOperation("直接转账（慎用）")
    @PostMapping("/transfer")
    public void transfer(@RequestBody @Valid WithdrawalTradeRequest request, @RequestParam String password) {
        if (!StringUtils.equals("wejnfdsao@$%#s3469", password)) {
            throw new BadRequestException("密码错误");
        }
        chargeService.transfer(request);
    }

    @ApiOperation("转账记录")
    @GetMapping("/transfer")
    public Slice<MerchantTransfer> getMerchantTransfer(@RequestParam(required = false, defaultValue = "") String user,
                                                       @RequestParam(required = false, defaultValue = "") WithdrawalType type,
                                                       @RequestParam(required = false, defaultValue = "") TradeStatus status,
                                                       @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                       @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                       @RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "10") long size) {
        return chargeService.getMerchantTransfer(user, type, status, start, end, page, size);
    }

}
