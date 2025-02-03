package com.example.restaurant_reservation.domain.reservation.repository;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservationRepositoryCustom {
    Page<ReservationResponseDto> reservationSearch(String name, String phone, LocalDateTime startTime, LocalDateTime endTime, String sortField, ReservationStatus status, Pageable pageable);
}
