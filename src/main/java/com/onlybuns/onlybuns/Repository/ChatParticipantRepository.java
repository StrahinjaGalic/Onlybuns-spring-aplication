package com.onlybuns.onlybuns.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onlybuns.onlybuns.Model.ChatParticipant;
import com.onlybuns.onlybuns.Model.User;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    
    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.chat.id = :chatId AND cp.user.username = :username")
    Optional<ChatParticipant> findByChatIdAndUsername(@Param("chatId") Long chatId, @Param("username") String username);
    
    @Query("SELECT cp.user FROM ChatParticipant cp WHERE cp.chat.id = :chatId")
    List<User> findUsersByChatId(@Param("chatId") Long chatId);
    
    @Query("SELECT cp.timeJoined FROM ChatParticipant cp WHERE cp.chat.id = :chatId AND cp.user.username = :username")
    Optional<Timestamp> findTimeJoinedByChatIdAndUsername(@Param("chatId") Long chatId, @Param("username") String username);

    void deleteByChatIdAndUserId(Long chatId, Long id);

    @Query("SELECT COUNT(cp) FROM ChatParticipant cp WHERE cp.chat.id = :chatId")
    long countByChatId(@Param("chatId") Long chatId);
}
