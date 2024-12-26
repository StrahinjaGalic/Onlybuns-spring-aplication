package com.onlybuns.onlybuns.Messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FANOUT_EXCHANGE = "posts.fanout";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Queue agencyQueue1() {
        return new Queue("agency.queue1",true);
    }

    @Bean
    public Queue agencyQueue2() {
        return new Queue("agency.queue2",true);
    }

    @Bean
    public Binding binding1(FanoutExchange fanoutExchange, Queue agencyQueue1) {
        return BindingBuilder.bind(agencyQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding binding2(FanoutExchange fanoutExchange, Queue agencyQueue2) {
        return BindingBuilder.bind(agencyQueue2).to(fanoutExchange);
    }
}
