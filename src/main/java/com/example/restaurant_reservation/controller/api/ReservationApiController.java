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


    @GetMapping("{reservationId}")
    public ResponseEntity<ReservationResponseDto> findById(@PathVariable Long reservationId,
                                                           Authentication authentication){

        // 現在ログインしているユーザー情報を取得する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.findById(reservationId);

        //本人の要約確認
        if (!reservation.getUserId().equals(userId)){
            throw new AccessDeniedException("You cannot access another user's reservation.");
        }

        return ResponseEntity.ok(reservation);
    }

    @PutMapping("{reservationId}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long reservationId,
                                                                    @RequestBody ReservationUpdateDto updateDto,
                                                                    Authentication authentication){
        // 現在ログインしているユーザー情報を取得する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.updateReservation(reservationId, updateDto);

        //本人の要約確認
        if (!reservation.getUserId().equals(userId)){
            throw new AccessDeniedException("You cannot access another user's reservation.");
        }

        return ResponseEntity.ok(reservation);
    }



}
