package com.wejuai.console.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.request.SendSystemMessageRequest;
import com.wejuai.console.repository.mongo.ChatUserRecordRepository;
import com.wejuai.console.repository.mongo.SystemMessageRepository;
import com.wejuai.console.repository.mysql.UserRepository;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.SystemMessage;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZM.Wang
 */
@Service
public class MessageService {

    private final UserRepository userRepository;
    private final SystemMessageRepository systemMessageRepository;
    private final ChatUserRecordRepository chatUserRecordRepository;

    private final MongoService mongoService;

    public MessageService(UserRepository userRepository, SystemMessageRepository systemMessageRepository, ChatUserRecordRepository chatUserRecordRepository, MongoService mongoService) {
        this.userRepository = userRepository;
        this.systemMessageRepository = systemMessageRepository;
        this.chatUserRecordRepository = chatUserRecordRepository;
        this.mongoService = mongoService;
    }

    @Transactional
    public void sendSystemMessage(SendSystemMessageRequest request) {
        if (request.getGroupPush()) {
            Set<SystemMessage> systemMessages = userRepository.findAllUserId().stream()
                    .map(userId -> new SystemMessage(request.getText(), userId, true)).collect(Collectors.toSet());
            systemMessageRepository.saveAll(systemMessages);
            userRepository.addAllUserMsgNum();
        } else {
            if (StringUtils.isBlank(request.getUserId())) {
                throw new BadRequestException("非群发userId必填");
            }
            sendSystemMessage(request.getUserId(), request.getText(), false);
        }
    }

    @SuppressWarnings("SameParameterValue")
    void sendSystemMessage(String userId, String text, boolean groupPush) {
        systemMessageRepository.save(new SystemMessage(text, userId, groupPush));
        userRepository.addUserMsgNum(userId);
    }

    public Slice<SystemMessage> getSystemMessages(String userId, Boolean groupPush, LocalDate start, LocalDate end, long page, long size) {
        Criteria criteria = new Criteria();
        if (groupPush != null) {
            criteria.and("groupPush").is(groupPush);
        }
        if (StringUtils.isNotBlank(userId)) {
            criteria.and("userId").is(userId);
        }
        if (start != null) {
            criteria.and("createdAt").gte(start);
        }
        if (end != null) {
            criteria.and("createdAt").lte(end);
        }
        long count = mongoService.getMongoPageCount(criteria, SystemMessage.class);
        List<SystemMessage> systemMessages = mongoService.getList(criteria, page, size, SystemMessage.class, Sort.Direction.DESC, "createdAt");
        return new Slice<>(systemMessages, page, size, count);
    }

    public void recalculateUserMsgNum(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("没有该用户: " + userId));
        long msgNum = chatUserRecordRepository.sumUserUnreadMsg(userId);
        long sysMsgNum = systemMessageRepository.countByUserIdAndWatchFalse(userId);
        if (msgNum + sysMsgNum != user.getMsgNum()) {
            userRepository.save(user.setMsgNum(msgNum + sysMsgNum));
        }
    }
}
