package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/payment")
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    /**
     * お支払いを作成
     */
     @PostMapping
    public ResponseEntity<PaymentResponseDto>createPayment(@RequestBody PaymentRequestDto requestDto,
                                                           Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        PaymentResponseDto payment = paymentService.createPayment(userId,requestDto);

        URI location = URI.create("/api/payment/" + payment.getId());
        return ResponseEntity.created(location).body(payment);
    }

    /**
     * お支払いの情報を照会
     */
    @GetMapping("/me")
    public ResponseEntity<List<PaymentResponseDto>>findByPaymentId(Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        List<PaymentResponseDto> payment = paymentService.findByOwnerId(userId);


        return ResponseEntity.ok(payment);
    }


    // お支払いをキャンセル
    @DeleteMapping
    public ResponseEntity<Void>deletePayment(Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        //ユーザーとして、お支払いの情報を照会
        PaymentResponseDto payment = paymentService.findById(userId);

        if (!"CANCELED".equals(payment.getStatus())){
            throw new IllegalStateException("キャンセルされたお支払いのみ削除できます。");
        }

        paymentService.deletePayment(userId);

        return ResponseEntity.noContent().build();
    }
}
