package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminApiController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    //USER- ADMIN

    // ユーザーをサーチ　
    @GetMapping("/user/search-user")
    public ResponseEntity<Page<UserResponseDto>> searchUser(@RequestParam(required = false) String username,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false)String phone,
                                                            @RequestParam(required = false) Pageable pageable){
        Page<UserResponseDto> user = userService.SearchUser(username, name, phone, pageable);

        return ResponseEntity.ok(user);
    }

    //ユーザーを削除
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userid){

        userService.deleteUser(userid);

        return ResponseEntity.noContent().build();
    }

    //RESERVATION - ADMIN

    @GetMapping("/reservation/search")
    public ResponseEntity<Page<ReservationResponseDto>> searchReservation(@RequestParam(required = false) String name,
                                                                          @RequestParam(required = false) String phone,
                                                                          @RequestParam(required = false) LocalDateTime startTime,
                                                                          @RequestParam(required = false) LocalDateTime endTime,
                                                                          @RequestParam(required = false) ReservationStatus status,
                                                                          @RequestParam(required = false) String sortField,
                                                                          @RequestParam(required = false) Pageable pageable){

        Page<ReservationResponseDto> reservation = reservationService.searchReservation(name, phone, startTime, endTime, sortField, status, pageable);

        return ResponseEntity.ok(reservation);
    }

    //PAYMENT - ADMIN
    @GetMapping("/payment/search")
    public ResponseEntity<Page<PaymentResponseDto>>paymentSearch(@RequestParam(required = false) Long reservationId,
                                                                 @RequestParam(required = false) PaymentStatus status,
                                                                 @RequestParam(required = false) Pageable pageable){

        Page<PaymentResponseDto> payment = paymentService.paymentSearch(reservationId, status, pageable);

        return ResponseEntity.ok(payment);
    }
}
