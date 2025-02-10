package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

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

    @GetMapping
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
