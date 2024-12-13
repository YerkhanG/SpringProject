package com.example.project.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @RabbitListener(queues = "notifications")
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}
