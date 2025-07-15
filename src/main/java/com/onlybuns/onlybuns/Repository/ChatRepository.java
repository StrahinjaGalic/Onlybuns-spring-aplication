package com.onlybuns.onlybuns.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onlybuns.onlybuns.Model.Chat;
import com.onlybuns.onlybuns.Model.ChatParticipant;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Chat findById(long id);
}
