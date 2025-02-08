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

import java.awt.desktop.PrintFilesEvent;
import java.nio.channels.Pipe;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurantTable_id", nullable = false)
    private RestaurantTable restaurantTable;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    private int numPeople;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


    @Builder
    public Reservation(User user, RestaurantTable restaurantTable, LocalDateTime reservationTime, int numPeople, ReservationStatus status) {
        this.user = user;
        this.restaurantTable = restaurantTable;
        this.reservationTime = reservationTime;
        this.numPeople = numPeople;
        this.status = status;
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
