package com.onlybuns.onlybuns.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.Model.Message;


public interface MessageRepository extends JpaRepository<Message,Long> {
    Message findById(long id);
    List<Message> findBySenderUsername(String senderUsername);
    List<Message> findByChatIdAndIsDeletedFalse(Long chatId);
    
}
