package com.onlybuns.onlybuns.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {
    
    @Id
    @GeneratedValue
    private Long id;

    private Boolean isGroupChat;

    private String adminName;
    private Boolean isDeleted = false;
}
