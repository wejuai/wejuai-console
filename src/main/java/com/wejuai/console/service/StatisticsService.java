package com.wejuai.console.service;

import com.wejuai.console.controller.dto.response.HobbyHotInfo;
import com.wejuai.console.controller.dto.response.SChartInfo;
import com.wejuai.console.repository.mongo.*;
import com.wejuai.entity.mongo.statistics.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.wejuai.console.config.Constant.*;
import static com.wejuai.entity.mongo.statistics.ChargeStatisticsType.*;

/**
 * @author ZM.Wang
 */
@Service
public class StatisticsService {

    private final HobbyTotalHotRepository hobbyTotalHotRepository;
    private final HobbyHotByDayRepository hobbyHotByDayRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final OrdersStatisticsRepository ordersStatisticsRepository;
    private final ChargeStatisticsRepository chargeStatisticsRepository;
    private final HobbyTotalHotByDayRepository hobbyTotalHotByDayRepository;
    private final UserStatisticsByDayRepository userStatisticsByDayRepository;
    private final ChargeStatisticsByDayRepository chargeStatisticsByDayRepository;
    private final OrdersStatisticsByDayRepository ordersStatisticsByDayRepository;

    public StatisticsService(OrdersStatisticsRepository ordersStatisticsRepository, ChargeStatisticsRepository chargeStatisticsRepository, UserStatisticsRepository userStatisticsRepository, HobbyTotalHotRepository hobbyTotalHotRepository, HobbyHotByDayRepository hobbyHotByDayRepository, ChargeStatisticsByDayRepository chargeStatisticsByDayRepository, OrdersStatisticsByDayRepository ordersStatisticsByDayRepository, UserStatisticsByDayRepository userStatisticsByDayRepository, HobbyTotalHotByDayRepository hobbyTotalHotByDayRepository) {
        this.ordersStatisticsRepository = ordersStatisticsRepository;
        this.chargeStatisticsRepository = chargeStatisticsRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.hobbyTotalHotRepository = hobbyTotalHotRepository;
        this.hobbyHotByDayRepository = hobbyHotByDayRepository;
        this.chargeStatisticsByDayRepository = chargeStatisticsByDayRepository;
        this.ordersStatisticsByDayRepository = ordersStatisticsByDayRepository;
        this.userStatisticsByDayRepository = userStatisticsByDayRepository;
        this.hobbyTotalHotByDayRepository = hobbyTotalHotByDayRepository;
    }

    public void addTransferOrders(boolean income, long amount) {
        if (income) {
            ordersStatisticsRepository
                    .save(getOrders().addTransferAddCount().addTransferAddAmount(amount));
        } else {
            ordersStatisticsRepository
                    .save(getOrders().addTransferSubCount().addTransferSubAmount(amount));
        }
    }

    public void addCharge(ChargeStatisticsType type, long amount) {
        if (type == RECHARGE) {
            chargeStatisticsRepository
                    .save(getCharge().addRechargeCount().addRechargeAmount(amount));
        }
        if (type == WITHDRAWAL) {
            chargeStatisticsRepository
                    .save(getCharge().addWithdrawalCount().addWithdrawalAmount(amount));
        }
        if (type == TRANSFER) {
            chargeStatisticsRepository
                    .save(getCharge().addTransferCount().addTransferAmount(amount));
        }
    }

    public OrdersStatistics getOrders() {
        return ordersStatisticsRepository.findById(ORDERS_STATISTICS_ID)
                .orElse(new OrdersStatistics(ORDERS_STATISTICS_ID));
    }

    public ChargeStatistics getCharge() {
        return chargeStatisticsRepository.findById(CHARGE_STATISTICS_ID)
                .orElse(new ChargeStatistics(CHARGE_STATISTICS_ID));
    }

    public UserStatistics getUser() {
        return userStatisticsRepository.findById(USER_STATISTICS_ID)
                .orElse(new UserStatistics(USER_STATISTICS_ID));
    }

    public HobbyTotalHot getHobby() {
        return hobbyTotalHotRepository.findById(HOBBY_TOTAL_HOT_ID)
                .orElse(new HobbyTotalHot(HOBBY_TOTAL_HOT_ID));
    }

    public SChartInfo chargeStatisticsByDays(LocalDate start, LocalDate end) {
        List<ChargeStatisticsByDay> data = chargeStatisticsByDayRepository.findByDateBetween(start, end);
        List<String> range = new ArrayList<>();
        List<Long> rechargeCount = new ArrayList<>();
        List<Long> rechargeAmount = new ArrayList<>();
        List<Long> transferCount = new ArrayList<>();
        List<Long> transferAmount = new ArrayList<>();
        List<Long> withdrawalCount = new ArrayList<>();
        List<Long> withdrawalAmount = new ArrayList<>();
        for (ChargeStatisticsByDay statistics : data) {
            range.add(statistics.getDate().toString());
            rechargeCount.add(statistics.getRechargeCount());
            rechargeAmount.add(statistics.getRechargeAmount());
            transferCount.add(statistics.getTransferCount());
            transferAmount.add(statistics.getTransferAmount());
            withdrawalCount.add(statistics.getWithdrawalCount());
            withdrawalAmount.add(statistics.getWithdrawalAmount());
        }
        return new SChartInfo(range)
                .addDates("成功充值单数量", rechargeCount)
                .addDates("成功充值单金额", rechargeAmount)
                .addDates("直接转账数量", transferCount)
                .addDates("直接转账金额", transferAmount)
                .addDates("成功提现单数量", withdrawalCount)
                .addDates("成功提现单金额", withdrawalAmount);
    }

    public Page<ChargeStatisticsByDay> chargeStatistics(LocalDate start, LocalDate end, Pageable pageable) {
        if (start == null || end == null) {
            return chargeStatisticsByDayRepository.findAll(pageable);
        }
        return chargeStatisticsByDayRepository.findByDateBetween(start, end, pageable);
    }

    public SChartInfo ordersStatisticsByDays(LocalDate start, LocalDate end) {
        List<OrdersStatisticsByDay> data = ordersStatisticsByDayRepository.findByDateBetween(start, end);
        List<String> dates = new ArrayList<>();
        List<Long> articleCount = new ArrayList<>();
        List<Long> articleAmount = new ArrayList<>();
        List<Long> rewardDemandCount = new ArrayList<>();
        List<Long> rewardDemandAmount = new ArrayList<>();
        List<Long> transferAddCount = new ArrayList<>();
        List<Long> transferAddAmount = new ArrayList<>();
        List<Long> transferSubCount = new ArrayList<>();
        List<Long> transferSubAmount = new ArrayList<>();
        for (OrdersStatisticsByDay statistics : data) {
            dates.add(statistics.getDate().toString());
            articleCount.add(statistics.getArticleCount());
            articleAmount.add(statistics.getArticleAmount());
            rewardDemandCount.add(statistics.getRewardDemandCount());
            rewardDemandAmount.add(statistics.getRewardDemandAmount());
            transferAddCount.add(statistics.getTransferAddCount());
            transferAddAmount.add(statistics.getTransferAddAmount());
            transferSubCount.add(statistics.getTransferSubCount());
            transferSubAmount.add(statistics.getTransferSubAmount());
        }
        return new SChartInfo(dates)
                .addDates("有积分文章订单数量", articleCount)
                .addDates("有积分文章订单积分数量", articleAmount)
                .addDates("完成的悬赏订单数量", rewardDemandCount)
                .addDates("完成的悬赏订单积分数量", rewardDemandAmount)
                .addDates("直接转账积分单数量", transferAddCount)
                .addDates("直接转账积分数量", transferAddAmount)
                .addDates("直接扣除积分单数量", transferSubCount)
                .addDates("直接扣除积分数量", transferSubAmount);
    }

    public Page<OrdersStatisticsByDay> ordersStatistics(LocalDate start, LocalDate end, Pageable pageable) {
        if (start == null || end == null) {
            return ordersStatisticsByDayRepository.findAll(pageable);
        }
        return ordersStatisticsByDayRepository.findByDateBetween(start, end, pageable);
    }

    public SChartInfo userStatisticsByDay(LocalDate start, LocalDate end) {
        List<UserStatisticsByDay> data = userStatisticsByDayRepository.findByDateBetween(start, end);
        List<String> dates = new ArrayList<>();
        List<Long> userRegisterCount = new ArrayList<>();
        List<Long> imMsgCount = new ArrayList<>();
        for (UserStatisticsByDay statistics : data) {
            dates.add(statistics.getDate().toString());
            userRegisterCount.add(statistics.getUserRegister());
            imMsgCount.add(statistics.getUserRegister());
        }
        return new SChartInfo(dates)
                .addDates("用户注册数", userRegisterCount)
                .addDates("聊天消息数", imMsgCount);
    }

    public Page<UserStatisticsByDay> userStatistics(LocalDate start, LocalDate end, Pageable pageable) {
        if (start == null || end == null) {
            return userStatisticsByDayRepository.findAll(pageable);
        }
        return userStatisticsByDayRepository.findByDateBetween(start, end, pageable);
    }

    public SChartInfo hobbyStatisticsByDay(String hobbyId, LocalDate start, LocalDate end) {
        List<String> dates = new ArrayList<>();
        List<Long> watched = new ArrayList<>();
        List<Long> commented = new ArrayList<>();
        List<Long> created = new ArrayList<>();
        List<Long> followed = new ArrayList<>();
        List<Long> unfollowed = new ArrayList<>();
        if (StringUtils.isNotBlank(hobbyId)) {
            List<HobbyHotByDay> data = hobbyHotByDayRepository.findByHobbyIdAndDateBetween(hobbyId, start, end);
            for (HobbyHotByDay statistics : data) {
                dates.add(statistics.getDate().toString());
                watched.add(statistics.getWatched());
                commented.add(statistics.getCommented());
                created.add(statistics.getCreated());
                followed.add(statistics.getFollowed());
                unfollowed.add(statistics.getUnfollowed());
            }
        } else {
            List<HobbyTotalHotByDay> data = hobbyTotalHotByDayRepository.findByDateBetween(start, end);
            for (HobbyTotalHotByDay statistics : data) {
                dates.add(statistics.getDate().toString());
                watched.add(statistics.getWatched());
                commented.add(statistics.getCommented());
                created.add(statistics.getCreated());
                followed.add(statistics.getFollowed());
                unfollowed.add(statistics.getUnfollowed());
            }
        }
        return new SChartInfo(dates)
                .addDates("阅读数", watched)
                .addDates("评论数", commented)
                .addDates("创建数", created)
                .addDates("关注数", followed)
                .addDates("取消关注数", unfollowed);
    }

    public Page<HobbyHotInfo> hobbyStatistics(String hobbyId, LocalDate start, LocalDate end, Pageable pageable) {
        if (start == null || end == null) {
            if (StringUtils.isNotBlank(hobbyId)) {
                return hobbyHotByDayRepository.findByHobbyId(hobbyId, pageable).map(HobbyHotInfo::new);
            }
            return hobbyTotalHotByDayRepository.findAll(pageable).map(HobbyHotInfo::new);
        } else {
            if (StringUtils.isNotBlank(hobbyId)) {
                return hobbyHotByDayRepository.findByHobbyIdAndDateBetween(hobbyId, start, end, pageable).map(HobbyHotInfo::new);
            }
            return hobbyTotalHotByDayRepository.findByDateBetween(start, end, pageable).map(HobbyHotInfo::new);
        }
    }

}
