package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentUpdateDto;
import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/payment")
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto>createPayment(@RequestBody PaymentRequestDto requestDto){
        PaymentResponseDto payment = paymentService.createPayment(requestDto);
        URI location = URI.create("/api/payment/" + payment.getId());
        return ResponseEntity.created(location).body(payment);
    }

    @GetMapping("{paymentId}")
    public ResponseEntity<PaymentResponseDto>findByPaymentId(@PathVariable Long paymentId){
        PaymentResponseDto payment = paymentService.findById(paymentId);

        return ResponseEntity.ok(payment);
    }



    @PutMapping("{paymentId}")
    public ResponseEntity<PaymentResponseDto>updatePayment(@PathVariable Long paymentId,
                                                            @RequestBody PaymentUpdateDto updateDto){

        PaymentResponseDto payment = paymentService.updatePayment(paymentId, updateDto);
       return ResponseEntity.ok(payment);
    }

    @DeleteMapping("{paymentId}")
    public ResponseEntity<Void>deletePayment(@PathVariable Long paymentId){

        paymentService.deletePayment(paymentId);

        return ResponseEntity.noContent().build();
    }
}
