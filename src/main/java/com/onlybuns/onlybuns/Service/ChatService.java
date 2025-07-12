package com.onlybuns.onlybuns.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Model.Chat;
import com.onlybuns.onlybuns.Model.Message;
import com.onlybuns.onlybuns.Repository.ChatRepository;
import com.onlybuns.onlybuns.Repository.MessageRepository;
import com.onlybuns.onlybuns.Repository.UserRepository;


@Service
public class ChatService {
    
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    public void createChat(String creatorUsername,String participantUsername) 
    {
        Chat chat = new Chat();
        chat.setAdminName(creatorUsername);
        chat.setIsGroupChat(false);
        chat.setIsDeleted(false);
        User creator = userRepository.findByUsername(creatorUsername);
        User participant = userRepository.findByUsername(participantUsername);

        if (creator == null || participant == null) {
            throw new IllegalArgumentException("Creator or participant does not exist");
        }
        
        chat.setParticipants(Arrays.asList(creator, participant));
        
        chatRepository.save(chat);
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
            User participant = userRepository.findByUsername(participantUsername);
            if (participant != null) 
            {
                if(chat.getParticipants().contains(participant)) 
                {
                    throw new IllegalArgumentException("Participant already exists in this chat");
                }
                chat.getParticipants().add(participant);
                chat.setIsGroupChat(true);
                chatRepository.save(chat);
            } 
            else 
            {
                throw new IllegalArgumentException("Participant does not exist");
            }
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    public void removeParticipantFromChat(Long chatId, String participantUsername,String username) 
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
                throw new IllegalArgumentException("Only the chat admin can remove participants");
            }
            User participant = userRepository.findByUsername(participantUsername);
            if (participant != null && chat.getParticipants().contains(participant)) 
            {
                chat.getParticipants().remove(participant);
                chatRepository.save(chat);
            } 
            else 
            {
                throw new IllegalArgumentException("Participant does not exist in this chat");
            }
        } 
        else 
        {
            throw new IllegalArgumentException("Chat does not exist");
        }
    }
    public void sendMessage(Long chatId, String senderUsername, String content) 
    {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            if (!chat.getParticipants().stream().anyMatch(user -> user.getUsername().equals(senderUsername))) 
            {
                throw new IllegalArgumentException("Sender is not a participant in this chat");
            }
            Message message = new Message();
            message.setChat(chat);
            message.setSenderUsername(senderUsername);
            message.setContent(content);
            message.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
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
            .filter(chat -> chat.getParticipants().contains(user))
            .collect(Collectors.toList());
        return chats;
    }
    public List<Message> findMessagesByChatId(Long chatId) 
    {
         Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) 
        {
            Chat chat = optionalChat.get();
            if (chat.getIsDeleted() != null && chat.getIsDeleted()) 
            {
                throw new IllegalArgumentException("Chat is deleted");
            }
            return messageRepository.findByChatIdAndIsDeletedFalse(chatId);
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
            return chatRepository.findParticipantsByChatId(chatId);
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
}
