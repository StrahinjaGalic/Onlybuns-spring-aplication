package com.onlybuns.onlybuns.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "follow")
@NoArgsConstructor           // Generates a no-argument constructor
@AllArgsConstructor          // Generates a constructor with parameters for all fields
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String followingUsername;
    
}
