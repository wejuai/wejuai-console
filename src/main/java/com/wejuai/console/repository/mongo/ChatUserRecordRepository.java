package com.wejuai.console.repository.mongo;

import com.wejuai.entity.mysql.ChatUserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatUserRecordRepository extends JpaRepository<ChatUserRecord, String> {

    @Query(nativeQuery = true, value = "SELECT IFNULL(SUM(unread_msg_num),0) from chat_user_record where del=false and recipient_id=?1")
    long sumUserUnreadMsg(String userId);

}
