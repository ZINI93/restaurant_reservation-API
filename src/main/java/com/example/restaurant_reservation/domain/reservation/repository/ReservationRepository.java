package com.example.restaurant_reservation.domain.reservation.repository;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> , ReservationRepositoryCustom{
    Optional<Reservation> findByUserId(Long userId);
}