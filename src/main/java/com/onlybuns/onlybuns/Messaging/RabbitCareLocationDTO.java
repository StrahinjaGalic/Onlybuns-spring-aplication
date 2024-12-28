package com.onlybuns.onlybuns.Messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitCareLocationDTO {
    private String id;
    private String name;
    private String location;
    private Double latitude;
    private Double longitude;
}

