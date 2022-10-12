package com.wejuai.console.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.repository.mysql.OrderAppealRepository;
import com.wejuai.console.repository.mysql.OrdersRepository;
import com.wejuai.console.repository.mysql.UserRepository;
import com.wejuai.entity.mysql.OrderAppeal;
import com.wejuai.entity.mysql.Orders;
import com.wejuai.entity.mysql.OrdersType;
import com.wejuai.entity.mysql.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author ZM.Wang
 * 订单管理，内部订单记录，和钱无关
 */
@Service
public class OrdersService {

    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final OrderAppealRepository orderAppealRepository;

    private final StatisticsService statisticsService;

    public OrdersService(OrdersRepository ordersRepository, UserRepository userRepository, OrderAppealRepository orderAppealRepository, StatisticsService statisticsService) {
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
        this.orderAppealRepository = orderAppealRepository;
        this.statisticsService = statisticsService;
    }

    public void passOrderAppeal(String id) {
        OrderAppeal orderAppeal = orderAppealRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该订单申诉: " + id));
        orderAppealRepository.save(orderAppeal.pass());
    }

    public void unPassOrderAppeal(String id, String rejectionReason) {
        OrderAppeal orderAppeal = orderAppealRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该订单申诉: " + id));
        orderAppealRepository.save(orderAppeal.notPass(rejectionReason));
    }

    @Transactional
    public void addIntegral(String userId, long integral) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("没有该用户: " + userId));
        userRepository.save(user.addIntegral(integral));
        ordersRepository.save(new Orders(OrdersType.SYSTEM_ADD, user, true, integral, OrdersType.SYSTEM_ADD.getName()));
        statisticsService.addTransferOrders(true, integral);
    }

    @Transactional
    public void subIntegral(String userId, long integral) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("没有该用户: " + userId));
        userRepository.save(user.cutIntegral(integral));
        ordersRepository.save(new Orders(OrdersType.SYSTEM_SUB, user, false, integral, OrdersType.SYSTEM_SUB.getName()));
        statisticsService.addTransferOrders(false, integral);
    }

}
