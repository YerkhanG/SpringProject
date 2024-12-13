package com.example.project.controller;

import com.example.project.model.Payment;
import com.example.project.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "oauth2",
        type = SecuritySchemeType.OAUTH2
)
@Tag(name = "Payment Management", description = "Endpoints for managing payments")
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RabbitTemplate rabbitTemplate;

    public PaymentController(PaymentService paymentService, RabbitTemplate rabbitTemplate) {
        this.paymentService = paymentService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Operation(summary = "Create a Payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment request",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody Payment payment) {
        payment.setCreatedAt(new Date());
        payment.setStatus("PENDING");

        // Сохраняем платеж в репозитории
        paymentService.createPayment(payment);

        // Отправляем платеж в очередь RabbitMQ
        rabbitTemplate.convertAndSend("payments", payment);

        return ResponseEntity.ok("Payment created and sent to queue: " + payment.getPaymentId());
    }

    @Operation(summary = "Get Payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content)
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @Operation(summary = "Get All Payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "204", description = "No payments found",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }
}

