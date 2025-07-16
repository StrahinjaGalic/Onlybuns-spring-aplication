package com.onlybuns.onlybuns.Controller;

import com.onlybuns.onlybuns.Dto.AddUserPayloadDto;
import com.onlybuns.onlybuns.Model.Message;
import com.onlybuns.onlybuns.Service.ChatService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    @Autowired
    private ChatService chatService;

    private final SimpMessageSendingOperations messagingTemplate;

    public ChatSocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        chatService.sendMessage
        (
            message.getChat().getId(),
            message.getSenderUsername(),
            message.getContent()
        );
        messagingTemplate.convertAndSend("/topic/chat." + message.getChat().getId(), message);
    }
}