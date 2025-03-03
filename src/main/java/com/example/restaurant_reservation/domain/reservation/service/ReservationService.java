package com.example.restaurant_reservation.domain.reservation.service;

import com.example.restaurant_reservation.domain.reservation.dto.AdminReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationRequestDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationUpdateDto;
import com.example.restaurant_reservation.domain.reservation.entity.ReservationStatus;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservationService {

    ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto, Long userId);
    ReservationResponseDto AdminCreateReservation(AdminReservationRequestDto reservationRequestDto);
    ReservationResponseDto findById(Long userId);
    ReservationResponseDto findByPaymentId(Long paymentId);

    ReservationResponseDto findByUuid(String uuid);
    Page<ReservationResponseDto> findAllByUserId(Long userId, Pageable pageable);
    Page<ReservationResponseDto> searchReservation(String name, String phone, LocalDateTime startTime, LocalDateTime endTime, String sortField, ReservationStatus status, Pageable pageable);
    ReservationResponseDto updateReservation(String userUuid, String reservationUuid,ReservationUpdateDto updateDto);
    void deleteReservation(String uuid);
}
