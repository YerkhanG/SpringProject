package com.example.project.service;

import com.example.project.model.Payment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final Map<String, Payment> paymentRepository = new HashMap<>();

    public void createPayment(Payment payment) {
        paymentRepository.put(payment.getPaymentId(), payment);
    }

    public Payment getPaymentById(String paymentId) {
        return paymentRepository.get(paymentId);
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentRepository.values());
    }
}

