package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/reservation")
@RestController
public class ReservationApiController {

    private final ReservationService reservationService;


    /**
     *  予約の登録
     *
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto,
                                                                    Authentication authentication){

        // CONTROLLER から、ユーザのIDを設定する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.createReservation(requestDto,userId);

        URI location = URI.create("/api/reservation/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }


    @GetMapping("/me")
    public ResponseEntity<ReservationResponseDto> findById(Authentication authentication){

        // 現在ログインしているユーザー情報を取得する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.findById(userId);

        return ResponseEntity.ok(reservation);
    }

    /**
     *  予約をアップデート
     */
    @PutMapping("/update")
    public ResponseEntity<ReservationResponseDto> updateReservation(@RequestBody ReservationUpdateDto updateDto,
                                                                    Authentication authentication){
        // 現在ログインしているユーザー情報を取得する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.updateReservation(userId, updateDto);

        return ResponseEntity.ok(reservation);
    }

}
