package com.example.restaurant_reservation.domain.reservation.dto;

import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {


    private Long id;
    private Long userId;
    private Long restaurantTableId;
    private LocalDateTime reservationTime;
    private int numPeople;
    private ReservationStatus status;

    @Builder
    public ReservationResponseDto(Long userId, Long restaurantTableId, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
        this.userId = userId;
        this.restaurantTableId = restaurantTableId;
        this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        this.status = status;
    }

    @QueryProjection
    public ReservationResponseDto(Long id, Long userId, Long restaurantTableId, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
        this.id = id;
        this.userId = userId;
        this.restaurantTableId = restaurantTableId;
        this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        this.status = status;
    }
}
