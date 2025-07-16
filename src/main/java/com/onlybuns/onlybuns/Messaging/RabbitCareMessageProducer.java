package com.onlybuns.onlybuns.Messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;



@Service
public class RabbitCareMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitCareMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRabbitCareMessage(String id, String name, String location, Double latitude, Double longitude) {
        RabbitCareLocationDTO message = new RabbitCareLocationDTO(id ,name, location, latitude, longitude);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, "rabbitCareKey", message);
        System.out.println("Message sent: " + message);
    }
    
}
