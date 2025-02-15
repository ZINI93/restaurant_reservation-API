package com.example.restaurant_reservation.domain.reservation.dto;

import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminReservationRequestDto {

        private Long userId;
        private Long restaurantTableId;
        private LocalDateTime reservationTime;
        private int numPeople;
        private ReservationStatus status;

        @Builder
        public AdminReservationRequestDto(Long userId, Long restaurantTableId, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
            this.userId = userId;
            this.restaurantTableId = restaurantTableId;
            this.reservationTime = reservationTime;
            this.numPeople = numPeople;
            this.status = status;
    }
}
