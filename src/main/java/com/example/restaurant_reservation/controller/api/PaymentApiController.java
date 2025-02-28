package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentUpdateDto;
import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    /**
     * お支払いを作成
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@Valid @RequestBody PaymentRequestDto requestDto,
                                                            Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        PaymentResponseDto payment = paymentService.createPayment(userId, requestDto);

        URI location = URI.create("/api/payment/" + payment.getId());
        return ResponseEntity.created(location).body(payment);
    }

    /**
     * お支払いの情報を照会
     */
    @GetMapping("/me")
    public ResponseEntity<List<PaymentResponseDto>> findByPaymentId(Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        List<PaymentResponseDto> payment = paymentService.findByOwnerId(userId);


        return ResponseEntity.ok(payment);
    }

    /**
     * お支払いアップデート
     */
    @PutMapping("{paymentUuid}")
    public ResponseEntity<PaymentResponseDto> updatePayment(@Valid @RequestBody PaymentUpdateDto updateDto,
                                                            @PathVariable String paymentUuid,
                                                            Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        PaymentResponseDto payment = paymentService.updatePayment(userId, paymentUuid, updateDto);

        return ResponseEntity.ok(payment);

    }


    /**
     * お支払い削除
     *  CANCELEDのみ削除を可能
     */

    @DeleteMapping("{paymentUuid}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentUuid,
                                              Authentication authentication) {

        log.info("Created Payment UUID:{}", paymentUuid);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        //ユーザーとして、お支払いの情報を
        PaymentResponseDto payment = paymentService.findByUuid(paymentUuid);

        if (Objects.equals(payment.getOwnerId(), userId)) {
            throw new AccessDeniedException("この支払いの削除権限がありません。");
        }


        if (!payment.getStatus().CANCELED.equals(payment.getStatus())) {
            throw new IllegalStateException("キャンセルされたお支払いのみ削除できます。");
        }

        paymentService.deletePayment(paymentUuid);

        return ResponseEntity.noContent().build();
    }
}
