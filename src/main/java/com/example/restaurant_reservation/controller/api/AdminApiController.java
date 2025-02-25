package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.payment.dto.PaymentResponseDto;
import com.example.restaurant_reservation.domain.payment.entity.PaymentStatus;
import com.example.restaurant_reservation.domain.payment.service.PaymentService;
import com.example.restaurant_reservation.domain.reservation.dto.AdminReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import com.example.restaurant_reservation.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminApiController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    /**
     *  USER - 管理者機能
     */

    // USERをサーチ　（USERNAME,NAME,PHONE)　
    @GetMapping("/user/search")  //?page=0&size=10
    public ResponseEntity<Page<UserResponseDto>> searchUser(@RequestParam(required = false) String username,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false)String phone,
                                                            @PageableDefault(size = 10, page = 0) Pageable pageable){
        Page<UserResponseDto> user = userService.SearchUser(username, name, phone, pageable);

        return ResponseEntity.ok(user);
    }

    //ユーザーを削除
    @DeleteMapping("/users/{userUuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userUuid){

        userService.deleteUser(userUuid);

        return ResponseEntity.noContent().build();
    }

    /**
     * 予約　ー　管理者機能
     */

    //予約を作る
    @PostMapping("reservation")
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody AdminReservationRequestDto requestDto,
                                                                    Authentication authentication){

        ReservationResponseDto reservation = reservationService.AdminCreateReservation(requestDto);

        URI location = URI.create("/api/reservation/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }


    /**
     *  予約のサーチ
     *  search : 名前、電話番号、予約時間、ステータス
     */
    @GetMapping("/reservation/search")
    public ResponseEntity<Page<ReservationResponseDto>> searchReservation(@RequestParam(required = false) String name,
                                                                          @RequestParam(required = false) String phone,
                                                                          @RequestParam(required = false) LocalDateTime startTime,
                                                                          @RequestParam(required = false) LocalDateTime endTime,
                                                                          @RequestParam(required = false) ReservationStatus status,
                                                                          @RequestParam(defaultValue = "reservationTime") String sortField,
                                                                          @PageableDefault(size = 20, page = 0) Pageable pageable){

        Page<ReservationResponseDto> reservation = reservationService.searchReservation(name, phone, startTime, endTime, sortField, status, pageable);

        return ResponseEntity.ok(reservation);
    }


    /**
     * 予約を削除
     */
    @DeleteMapping("/reservation/{reservationUuid}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String reservationUuid,
                                                  Authentication authentication){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.findByUuid(reservationUuid);

        if (Objects.equals(reservation.getUserId(),userId)){
            throw new AccessDeniedException("この予約の削除権限がありません。");
        }

        reservationService.deleteReservation(reservationUuid);

        return ResponseEntity.noContent().build();
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
