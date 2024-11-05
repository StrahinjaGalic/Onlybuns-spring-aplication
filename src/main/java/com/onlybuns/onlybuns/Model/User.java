package com.onlybuns.onlybuns.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;  //implement req constructor when necessery

@Data
@Table(name = "users")
@NoArgsConstructor           // Generates a no-argument constructor
@AllArgsConstructor          // Generates a constructor with parameters for all fields
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique IDs
    private Long id; // Primary key field
    private String email;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    private String name;
    private String address;
    private boolean isActive;
    
    @Enumerated(EnumType.STRING)
    public Role role;
    
    @Column(unique = true)
    private String activationToken;
}

