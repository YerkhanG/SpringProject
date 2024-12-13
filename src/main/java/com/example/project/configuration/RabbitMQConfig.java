package com.example.project.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue ordersQueue() {
        return new Queue("orders", true); // Durable queue
    }
    @Bean
    public Queue notificationsQueue() {
        return new Queue("notifications", true); // Durable queue
    }
}
