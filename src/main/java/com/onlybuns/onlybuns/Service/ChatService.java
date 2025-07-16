package com.onlybuns.onlybuns.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Model.Chat;
import com.onlybuns.onlybuns.Model.ChatParticipant;
import com.onlybuns.onlybuns.Model.Message;
import com.onlybuns.onlybuns.Repository.ChatParticipantRepository;
import com.onlybuns.onlybuns.Repository.ChatRepository;
import com.onlybuns.onlybuns.Repository.MessageRepository;
import com.onlybuns.onlybuns.Repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class ChatService {
    
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatParticipantRepository chatParticipantRepository;

    public void createChat(String creatorUsername,String participantUsername) 
    {
        Chat chat = new Chat();
        chat.setAdminName(creatorUsername);
        chat.setIsGroupChat(false);
        chat.setIsDeleted(false);
        
        // Sačuvaj chat PRVO da bi dobio ID
        Chat savedChat = chatRepository.save(chat);
        
        User creator = userRepository.findByUsername(creatorUsername);
        User participant = userRepository.findByUsername(participantUsername);

        if (creator == null || participant == null) {
            throw new IllegalArgumentException("Creator or participant does not exist");
        }
        ChatParticipant creatorParticipant = new ChatParticipant();
        creatorParticipant.setChat(savedChat);  // Koristi savedChat umesto chat
        creatorParticipant.setUser(creator);
        creatorParticipant.setTimeJoined(new java.sql.Timestamp(System.currentTimeMillis()));

        ChatParticipant participantParticipant = new ChatParticipant();
        participantParticipant.setChat(savedChat);  // Koristi savedChat umesto chat
        participantParticipant.setUser(participant);
        participantParticipant.setTimeJoined(new java.sql.Timestamp(System.currentTimeMillis()));

        chatParticipantRepository.save(creatorParticipant);
        chatParticipantRepository.save(participantParticipant);
    }
    public void addParticipantToChat(Long chatId, String participantUsername, String username) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            if(!chat.getAdminName().equals(username)) 
            {
                throw new IllegalArgumentException("Only the chat admin can add participants");
            }
            
            User participantUser = userRepository.findByUsername(participantUsername);
            if (participantUser == null) {
                throw new IllegalArgumentException("Participant does not exist");
            }
            
            if(chatParticipantRepository.findByChatIdAndUsername(chatId, participantUsername).isPresent()) {
                throw new IllegalArgumentException("Participant already exists in this chat");
            }
            
            ChatParticipant participant = new ChatParticipant();
            participant.setChat(chat);
            participant.setUser(participantUser);
            participant.setTimeJoined(new java.sql.Timestamp(System.currentTimeMillis()));
            
            chatParticipantRepository.save(participant);
            
            if (!chat.getIsGroupChat()) {
                chat.setIsGroupChat(true);
                chatRepository.save(chat);
            }
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    @Transactional
    public void removeParticipantFromChat(Long chatId, String participantUsername, String username) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat is deleted");
            }
            if(!chat.getAdminName().equals(username)) 
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the chat admin can remove participants");
            }
            
            User participant = userRepository.findByUsername(participantUsername);
            if (participant != null) 
            {
                if(chatParticipantRepository.findByChatIdAndUsername(chatId, participantUsername).isPresent()) 
                {
                    
                    chatParticipantRepository.deleteByChatIdAndUserId(chatId, participant.getId());
                    
                    long remainingParticipants = chatParticipantRepository.countByChatId(chatId);
                    if(remainingParticipants == 2) 
                    {
                        chat.setIsGroupChat(false);
                        chatRepository.save(chat);
                    }
                } 
                else 
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participant does not exist in this chat");
                }
            } 
            else 
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participant does not exist");
            }
        } 
        else 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat does not exist");
        }
    }
    public void sendMessage(Long chatId, String senderUsername, String content) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            Optional<ChatParticipant> optionalParticipant = chatParticipantRepository.findByChatIdAndUsername(chatId,senderUsername);

            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            if (!optionalParticipant.isPresent()) 
            {
                throw new IllegalArgumentException("Sender is not a participant in this chat");
            }
            Message message = new Message();
            message.setChat(chat);
            message.setSenderUsername(senderUsername);
            message.setContent(content);
            message.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
            message.setIsDeleted(false);
            messageRepository.save(message);
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    public List<Chat> findAllChatsByUser(String username) 
    {
        User user = userRepository.findByUsername(username);
        if (user == null) 
        {
            throw new IllegalArgumentException("User does not exist");
        }
        List<Chat> chats = chatRepository.findAll().stream()
            .filter(chat -> !Boolean.TRUE.equals(chat.getIsDeleted()))
            .filter(chat -> chatParticipantRepository.findByChatIdAndUsername(chat.getId(), username).isPresent())
            .collect(Collectors.toList());
        return chats;
    }
    public List<Message> findMessagesByChatId(Long chatId,String username) 
    {
         Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            Optional<ChatParticipant> chatParticipant = chatParticipantRepository.findByChatIdAndUsername(chat.getId(), username);
            if (!chatParticipant.isPresent()) 
            {
                throw new IllegalArgumentException("User is not a participant in this chat");
            }
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            Timestamp dateJoined = chatParticipant.get().getTimeJoined();
            List<Message> allMessages = messageRepository.findByChatIdAndIsDeletedFalse(chatId);

            allMessages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));

            List<Message> messagesBeforeJoined = allMessages.stream()
                .filter(message -> message.getTimestamp().before(dateJoined))
                .collect(Collectors.toList());
            List<Message> messagesAfterJoined = allMessages.stream()
                .filter(message -> message.getTimestamp().after(dateJoined) || message.getTimestamp().equals(dateJoined))
                .collect(Collectors.toList());

            List<Message> last10BeforeJoined = messagesBeforeJoined.stream()
            .skip(Math.max(0, messagesBeforeJoined.size() - 10))
            .collect(Collectors.toList());
        
            List<Message> finalMessages = new ArrayList<>();
            finalMessages.addAll(last10BeforeJoined);
            finalMessages.addAll(messagesAfterJoined);
            
            return finalMessages;    
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    public List<User> findParticipantsByChatId(Long chatId) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            return chatParticipantRepository.findUsersByChatId(chatId);
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    public void deleteChat(Long chatId, String username) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is already deleted");
            }
            if (!chat.getAdminName().equals(username)) 
            {
                throw new IllegalArgumentException("Only the chat admin can delete the chat");
            }
            chat.setIsDeleted(true);
            chatRepository.save(chat);
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    public void deleteMessage(Long messageId, String username) 
    {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) 
        {
            Message message = optionalMessage.get();
            if (message.getIsDeleted() != null && message.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Message is already deleted");
            }
            if (!message.getSenderUsername().equals(username)) 
            {
                throw new IllegalArgumentException("Only the message sender can delete the message");
            }
            message.setIsDeleted(true);
            messageRepository.save(message);
        } 
        else 
        {
            throw new IllegalArgumentException("Message does not exist");
        }
    }
    public String getAdminUsernameByChatId(Long chatId) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            return chat.getAdminName();
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
}
