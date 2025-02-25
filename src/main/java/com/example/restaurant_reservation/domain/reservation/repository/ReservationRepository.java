package com.example.restaurant_reservation.domain.reservation.repository;

import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> , ReservationRepositoryCustom{
    Optional<Reservation> findByUserId(Long userId);

    Optional<Reservation> findByUserUserUuid(String userUuid);

    Optional<Reservation> findByReservationUuid(String uuid);

    Page<Reservation> findAllByUserId(Long userId, Pageable pageable);

    boolean existsByRestaurantTableIdAndReservationTime(Long id, LocalDateTime requestedTime);
}