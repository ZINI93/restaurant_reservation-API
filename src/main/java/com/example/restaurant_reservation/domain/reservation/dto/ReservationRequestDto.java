package com.example.restaurant_reservation.domain.reservation.dto;

import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {

    @NotNull
    private Long restaurantTableId;

    @NotNull
    private LocalDateTime reservationTime;

    @NotNull
    private int numPeople;

    @NotBlank(message = "予約のステータスが必要です。")
    private ReservationStatus status;


    @Builder
    public ReservationRequestDto(Long restaurantTableId, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
        this.restaurantTableId = restaurantTableId;
        this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        this.status = status;
    }
}