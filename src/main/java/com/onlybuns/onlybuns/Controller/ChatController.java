package com.onlybuns.onlybuns.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.Dto.AddUserPayloadDto;
import com.onlybuns.onlybuns.Dto.RemoveUserPayloadDto;
import com.onlybuns.onlybuns.Model.Chat;
import com.onlybuns.onlybuns.Model.Message;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Service.ChatService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping("/create")
    public void createChat(@RequestBody Map<String, String> payload) {
        String creatorUsername = payload.get("creatorUsername");
        String participantUsername = payload.get("participantUsername");

        chatService.createChat(creatorUsername, participantUsername);
    }
    
    @GetMapping("/user/{username}")
    public List<Chat> getUserChats(@PathVariable String username) {
        return chatService.findAllChatsByUser(username);
    }
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Long chatId) {
        List<Message> messages = chatService.findMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> addUserToChat(@RequestBody AddUserPayloadDto payload) {
        
        chatService.addParticipantToChat(
            payload.getChatId(),
            payload.getUsernameToAdd(),
            payload.getAdminUsername()
        );
        return ResponseEntity.ok().build();
    }
    @PostMapping("/removeUser")
    public ResponseEntity<?> removeUserFromChat(@RequestBody RemoveUserPayloadDto payload) {
        chatService.removeParticipantFromChat(
            payload.getChatId(),
            payload.getUsernameToRemove(),
            payload.getAdminUsername()
        );
        return ResponseEntity.ok().build();
    }
    @GetMapping("/participants/{chatId}")
    public ResponseEntity<List<User>> getMethodName(@PathVariable String chatId) 
    {
        return ResponseEntity.ok(chatService.findParticipantsByChatId(Long.parseLong(chatId)));
    }
    @GetMapping("/getAdmin/{chatId}")
    public ResponseEntity<String> getAdminUsername(@PathVariable String chatId) {
        return ResponseEntity.ok(chatService.getAdminUsernameByChatId(Long.parseLong(chatId)));
    }
    
    

}
