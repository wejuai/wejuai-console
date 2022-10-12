package com.wejuai.console.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.repository.mongo.ChargeRepository;
import com.wejuai.console.repository.mongo.SystemMessageRepository;
import com.wejuai.console.repository.mysql.OrdersRepository;
import com.wejuai.console.repository.mysql.UserRepository;
import com.wejuai.console.repository.mysql.WithdrawalRepository;
import com.wejuai.console.support.TradeGatewayClient;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.request.WithdrawalTradeRequest;
import com.wejuai.dto.response.ChargeListInfo;
import com.wejuai.dto.response.MchTradeResponse;
import com.wejuai.dto.response.Slice;
import com.wejuai.dto.response.UserIntegralInfo;
import com.wejuai.dto.response.WithdrawalInfo;
import com.wejuai.entity.mongo.MerchantTransfer;
import com.wejuai.entity.mongo.SystemMessage;
import com.wejuai.entity.mongo.statistics.ChargeStatisticsType;
import com.wejuai.entity.mongo.trade.Charge;
import com.wejuai.entity.mongo.trade.TradeStatus;
import com.wejuai.entity.mysql.Accounts;
import com.wejuai.entity.mysql.ChannelQueryType;
import com.wejuai.entity.mysql.Orders;
import com.wejuai.entity.mysql.OrdersType;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.Withdrawal;
import com.wejuai.entity.mysql.WithdrawalType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ZM.Wang
 * 外部交易相关，和钱有关系
 */
@Service
public class ChargeService {

    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final ChargeRepository chargeRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final SystemMessageRepository systemMessageRepository;

    private final WejuaiCoreClient wejuaiCoreClient;
    private final WxMessageService wxMessageService;
    private final StatisticsService statisticsService;
    private final TradeGatewayClient tradeGatewayClient;

    private final MongoService mongoService;

    public ChargeService(WithdrawalRepository withdrawalRepository, WejuaiCoreClient wejuaiCoreClient, SystemMessageRepository systemMessageRepository, UserRepository userRepository, OrdersRepository ordersRepository, ChargeRepository chargeRepository, TradeGatewayClient tradeGatewayClient, WxMessageService wxMessageService, StatisticsService statisticsService, MongoService mongoService) {
        this.withdrawalRepository = withdrawalRepository;
        this.wejuaiCoreClient = wejuaiCoreClient;
        this.systemMessageRepository = systemMessageRepository;
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
        this.chargeRepository = chargeRepository;
        this.tradeGatewayClient = tradeGatewayClient;
        this.wxMessageService = wxMessageService;
        this.statisticsService = statisticsService;
        this.mongoService = mongoService;
    }

    public WithdrawalInfo getWithdrawalInfo(String id) {
        Withdrawal withdrawal = getWithdrawal(id);
        return new WithdrawalInfo(withdrawal);
    }

    public UserIntegralInfo syncUserIntegral(String userId) {
        return wejuaiCoreClient.sumUserWithdrawableIntegral(userId);
    }

    @Transactional
    public void passWithdrawal(String id) {
        Withdrawal withdrawal = getWithdrawal(id);
        long withdrawalIntegral = withdrawal.getIntegral();
        UserIntegralInfo userIntegralInfo = wejuaiCoreClient.sumUserWithdrawableIntegral(withdrawal.getUser().getId());
        if (userIntegralInfo.getTotal() + userIntegralInfo.getProcessingWithdrawal() < withdrawalIntegral) {
            throw new BadRequestException("可提现积分不足");
        }
        long actualWithdrawalIntegral = withdrawalIntegral * 93 / 100;
        WithdrawalTradeRequest request = new WithdrawalTradeRequest(withdrawal.getUser().getId(),
                withdrawal.getChannelType(), "积分提现，规则：金额(元)=积分/100*93%，您的：" + withdrawalIntegral + "/100*93%", actualWithdrawalIntegral,
                withdrawal.getCardNo(), withdrawal.getName());

        String ip = withdrawal.getUser().getAccounts().getIp();
        MchTradeResponse mchTradeResponse = tradeGatewayClient.merchantPay(request, ip);
        withdrawalRepository.save(withdrawal.complete(mchTradeResponse.getStatus(), mchTradeResponse.getMchTradeRecordId(), mchTradeResponse.getChannelTradeId()));
        new Thread(() -> wxMessageService.sendWithdrawalAuditMsg(withdrawal)).start();
        new Thread(() -> statisticsService.addCharge(ChargeStatisticsType.WITHDRAWAL, withdrawalIntegral)).start();
    }

    @Transactional
    public void unPassWithdrawal(String id, String rejectionReason) {
        Withdrawal withdrawal = getWithdrawal(id);
        User user = withdrawal.getUser();
        withdrawalRepository.save(withdrawal.rejection(rejectionReason));
        Orders orders = new Orders(OrdersType.CASH_WITHDRAWAL_RETURN, user, true, withdrawal.getIntegral(), "提现返还");
        ordersRepository.save(orders);
        userRepository.save(user.addIntegral(withdrawal.getIntegral()).addMsg());
        systemMessageRepository.save(new SystemMessage("申请的提现被驳回，并将积分返还，驳回原因为：" + rejectionReason, user.getId(), false));
        new Thread(() -> wxMessageService.sendWithdrawalAuditMsg(withdrawal)).start();
    }

    public Slice<ChargeListInfo> getCharges(String id, String userId, TradeStatus status, ChannelQueryType channelType, Long start, Long end, int page, int size) {
        if (StringUtils.isNotBlank(id)) {
            Charge charge = chargeRepository.findById(id).orElse(null);
            Page<ChargeListInfo> charges = new PageImpl<>(charge == null ? Collections.emptyList() : Collections.singletonList(new ChargeListInfo(charge)));
            return new Slice<>(charges);
        } else {
            return wejuaiCoreClient.getChargeInfos(userId, status, channelType, start, end, page, size);
        }
    }

    public void transfer(WithdrawalTradeRequest request) {
        if (request.getType() == WithdrawalType.ALIPAY) {
            throw new BadRequestException("直接转账无法知道用户支付宝信息");
        }
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BadRequestException("没有该用户: " + request.getUserId()));
        Accounts accounts = user.getAccounts();
        if (accounts.getWeixinUser() == null || StringUtils.isBlank(accounts.getWeixinUser().getAppOpenId())) {
            throw new BadRequestException("该用户尚未绑定微信小程序, 可以发送系统消息提醒用户绑定");
        }
        request.addCardInfo(accounts.getWeixinUser().getAppOpenId(), null).setDesc("系统发放");
        tradeGatewayClient.merchantPay(request, accounts.getIp());
    }

    public Slice<MerchantTransfer> getMerchantTransfer(String user, WithdrawalType type, TradeStatus status, LocalDate start, LocalDate end, long page, long size) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(user)) {
            criteria.and("user").is(user);
        }
        if (type != null) {
            criteria.and("type").is(type);
        }
        if (status != null) {
            criteria.and("status").is(status);
        }
        if (start != null) {
            criteria.and("createdAt").gte(start);
        }
        if (end != null) {
            criteria.and("createdAt").lte(end);
        }
        long count = mongoService.getMongoPageCount(criteria, MerchantTransfer.class);
        List<MerchantTransfer> merchantTransfers = mongoService.getList(criteria, page, size, MerchantTransfer.class, Sort.Direction.DESC, "createdAt");
        return new Slice<>(merchantTransfers, page, size, count);
    }

    private Withdrawal getWithdrawal(String id) {
        Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(id);
        if (withdrawalOptional.isEmpty()) {
            throw new BadRequestException("没有该提现记录：" + id);
        }
        return withdrawalOptional.get();
    }

}
