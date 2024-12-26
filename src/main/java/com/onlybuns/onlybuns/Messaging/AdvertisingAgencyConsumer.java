package com.onlybuns.onlybuns.Messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AdvertisingAgencyConsumer {

    @RabbitListener(queues = "agency.queue1")
    public void receiveMessageFromQueue1(String message) {
        System.out.println("Agency 1 received: " + message);
    }

    @RabbitListener(queues = "agency.queue2")
    public void receiveMessageFromQueue2(String message) {
        System.out.println("Agency 2 received: " + message);
    }
}
