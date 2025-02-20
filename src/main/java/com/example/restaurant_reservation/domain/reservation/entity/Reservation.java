package com.example.restaurant_reservation.domain.reservation.entity;

import com.example.restaurant_reservation.domain.TimeStamp;
import com.example.restaurant_reservation.domain.reservation.dto.ReservationResponseDto;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation")
@Entity
public class Reservation extends TimeStamp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_table_id")
    private RestaurantTable restaurantTable;

    @Column(name = "reservation_time",nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "num_people", nullable = false)
    private int numPeople;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(nullable = false, unique = true, updatable = false, name = "reservation_uuid")
    private String reservationUuid;

    @Builder
    public Reservation(User user, RestaurantTable restaurantTable, LocalDateTime reservationTime, int numPeople, ReservationStatus status,String reservationUuid) {
        this.user = user;
        this.restaurantTable = restaurantTable;
        this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        this.status = status;
        this.reservationUuid = UUID.randomUUID().toString();
    }

    public ReservationResponseDto toResponse(){
        return ReservationResponseDto.builder()
                .userId(this.user.getId())
                .restaurantTableId(this.restaurantTable.getId())
                .reservationTime(this.getReservationTime())
                .numPeople(this.numPeople)
                .status(this.status)
                .build();
    }

    public void updateInfo(RestaurantTable restaurantTable, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
        if (restaurantTable != null) this.restaurantTable = restaurantTable;
        if (reservationTime != null) this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        if (status != null) this.status = status;
    }
}
