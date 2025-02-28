package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@RestController
public class ReservationApiController {

    private final ReservationService reservationService;


    /**
     *  予約の登録
     *
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto,
                                                                    Authentication authentication){

        // CONTROLLER から、ユーザのIDを設定する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        ReservationResponseDto reservation = reservationService.createReservation(requestDto,userId);

        URI location = URI.create("/api/reservation/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }


    @GetMapping("/me")
    public ResponseEntity<Page<ReservationResponseDto>> findById(Authentication authentication,
                                                                @PageableDefault(size = 10 , page = 0) Pageable pageable){

        // 現在ログインしているユーザー情報を取得する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        Page<ReservationResponseDto> reservations = reservationService.findAllByUserId(userId, pageable);

        return ResponseEntity.ok(reservations);
    }


    /**
     *  予約をアップデート
     */
    @PutMapping("{reservationUuid}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@Valid @RequestBody ReservationUpdateDto updateDto,
                                                                    @PathVariable String reservationUuid,
                                                                    Authentication authentication){
        // 現在ログインしているユーザー情報を取得する
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userUuid = customUserDetails.getUserUuid();

        ReservationResponseDto reservation = reservationService.updateReservation(userUuid, reservationUuid, updateDto);

        return ResponseEntity.ok(reservation);
    }

}
