package com.example.project.service;

import com.example.project.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    private final RabbitTemplate rabbitTemplate;

    public InventoryListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "orders")
    public void processOrder(Order order) {
        // Simulate inventory update
        System.out.println("Processing order: " + order.getOrderId());
        rabbitTemplate.convertAndSend("notifications", "Order processed: " + order.getOrderId());
    }
}

