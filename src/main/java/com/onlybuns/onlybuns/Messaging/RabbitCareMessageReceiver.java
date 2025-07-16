package com.onlybuns.onlybuns.Messaging;

import com.onlybuns.onlybuns.Model.RabbitCare;
import com.onlybuns.onlybuns.Repository.RabbitCareRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RabbitCareMessageReceiver {

    private final RabbitCareRepository repository;

    public RabbitCareMessageReceiver(RabbitCareRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "rabbit.care.queue")
    public void receiveRabbitCareMessage(@Payload RabbitCareLocationDTO message) {
        try {
            System.out.println("Received Rabbit Care Message: " + message);
        
            // Convert DTO to Entity and save it
            RabbitCare rabbitCare = new RabbitCare();
            rabbitCare.setServiceId(Long.parseLong(message.getId()));  // Ensure id is convertible to Long
            rabbitCare.setName(message.getName());
            rabbitCare.setLocation(message.getLocation());
            rabbitCare.setLatitude(message.getLatitude());
            rabbitCare.setLongitude(message.getLongitude());
        
            repository.save(rabbitCare);
        
        } catch (Exception e) {
            System.out.println("Error processing the message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}


