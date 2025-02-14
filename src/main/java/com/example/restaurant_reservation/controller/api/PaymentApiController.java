package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.payment.dto.PaymentRequestDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.dto.PaymentUpdateDto;
import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import com.example.restaurant_reservation.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/payment")
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto>createPayment(@RequestBody PaymentRequestDto requestDto){
        PaymentResponseDto payment = paymentService.createPayment(requestDto);
        URI location = URI.create("/api/payment/" + payment.getId());
        return ResponseEntity.created(location).body(payment);
    }

    @GetMapping("{paymentId}")
    public ResponseEntity<PaymentResponseDto>findByPaymentId(@PathVariable Long paymentId,
                                                             Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        //支払いの情報を照会
        PaymentResponseDto payment = paymentService.findById(paymentId);

        //予約情報からuserIdを抽出して確認する
        ReservationResponseDto reservation = reservationService.findByPaymentId(paymentId);

        //予約情報が存在しない場合の処理
        if (reservation == null){
            throw new IllegalArgumentException("この支払いには対応する予約がありません。");
        }

        Long reservationUserId = reservation.getUserId();

        //支払いのユーザが合ってるか照会
        if (!reservationUserId.equals(userId)){
            throw new AccessDeniedException("You cannot access another user's payment information.");
        }

        return ResponseEntity.ok(payment);
    }


    // お支払いをキャンセル
    @DeleteMapping("{paymentId}")
    public ResponseEntity<Void>deletePayment(@PathVariable Long paymentId,
                                             Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        paymentService.deletePayment(paymentId);

        //予約情報からuserIdを抽出して確認する
        ReservationResponseDto reservation = reservationService.findByPaymentId(paymentId);

        //予約情報が存在しない場合の処理
        if (reservation == null){
            throw new IllegalArgumentException("この支払いには対応する予約がありません。");
        }

        Long reservationUserId = reservation.getUserId();

        //支払いのユーザが合ってるか照会
        if (!reservationUserId.equals(userId)){
            throw new AccessDeniedException("You cannot access another user's payment information.");
        }

        return ResponseEntity.noContent().build();
    }
}
