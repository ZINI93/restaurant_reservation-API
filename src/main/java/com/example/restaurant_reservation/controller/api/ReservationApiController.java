package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/reservation")
@RestController
public class ReservationApiController {

    private final ReservationService reservationService;


    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto){

        ReservationResponseDto reservation = reservationService.createReservation(requestDto);
        URI location = URI.create("/api/reservation/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }

    @GetMapping("{reservationId}")
    public ResponseEntity<ReservationResponseDto> findById(@PathVariable Long reservationId){

        ReservationResponseDto reservation = reservationService.findById(reservationId);

        return ResponseEntity.ok(reservation);
    }

    @PutMapping("{reservationId}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long reservationId,
                                                                    @RequestBody ReservationUpdateDto updateDto){

        ReservationResponseDto reservation = reservationService.updateReservation(reservationId, updateDto);

        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId){

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.noContent().build();
    }

}
