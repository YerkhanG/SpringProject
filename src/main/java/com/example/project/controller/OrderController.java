package com.example.project.controller;

import com.example.project.model.Order;
import com.example.project.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final RabbitTemplate rabbitTemplate;
    private final OrderService orderService;

    public OrderController(RabbitTemplate rabbitTemplate,OrderService orderService) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        rabbitTemplate.convertAndSend("orders", order);
        return ResponseEntity.ok("Order placed: " + orderService.saveOrder(order).getOrderId());
    }


    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }
}