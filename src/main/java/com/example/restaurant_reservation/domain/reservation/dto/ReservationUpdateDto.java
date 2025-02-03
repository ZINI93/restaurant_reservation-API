package com.example.restaurant_reservation.domain.reservation.dto;

import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationUpdateDto {

    private Long restaurantTableId;
    private LocalDateTime reservationTime;
    private int numPeople;
    private ReservationStatus status;

    @Builder
    public ReservationUpdateDto(Long restaurantTableId, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
        this.restaurantTableId = restaurantTableId;
        this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        this.status = status;
    }
}
