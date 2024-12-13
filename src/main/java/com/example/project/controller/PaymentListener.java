package com.example.project.controller;

import com.example.project.model.Payment;
import com.example.project.model.Product;
import com.example.project.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaymentListener {
    private final ProductService productService;
    private final Set<String> processedPayments = ConcurrentHashMap.newKeySet(); // Хранение обработанных платежей

    public PaymentListener(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = "payments")
    public void handlePayment(Payment payment) {
        // Проверяем, был ли платеж уже обработан
        if (processedPayments.contains(payment.getPaymentId())) {
            System.out.println("Payment already processed: " + payment.getPaymentId());
            return;
        }

        System.out.println("Received payment: " + payment.getPaymentId());

        if (processPayment(payment)) {
            payment.setStatus("SUCCESS");
            System.out.println("Payment processed successfully: " + payment.getPaymentId());

            // Списываем количество продукта
            Product product = productService.getProductById(payment.getProductId());
            if (product != null && product.getQuantity() >= payment.getQuantity()) {
                product.setQuantity(product.getQuantity() - payment.getQuantity());
                productService.updateProduct(product);
                // Отмечаем платеж как обработанный
                processedPayments.add(payment.getPaymentId());
            } else {
                System.out.println("Insufficient product quantity for payment: " + payment.getPaymentId());
            }
        } else {
            payment.setStatus("FAILED");
            System.out.println("Payment failed: " + payment.getPaymentId());
        }
    }

    private boolean processPayment(Payment payment) {
        // Простая имитация обработки платежа
        return payment.getAmount() > 0;
    }
}
