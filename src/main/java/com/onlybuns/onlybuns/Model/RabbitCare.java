package com.onlybuns.onlybuns.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class RabbitCare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long serviceId;
    private String name;
    private String location;
    private Double latitude;
    private Double longitude;

    // Getters and Setters (or use Lombok @Data for simplicity)
}

