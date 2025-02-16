package com.example.restaurant_reservation.domain.reservation.dto;

import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminReservationRequestDto {

        @NotNull
        private Long userId;

        @NotNull
        private Long restaurantTableId;

        @NotNull
        private LocalDateTime reservationTime;

        @NotNull
        private int numPeople;

        @NotBlank
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
