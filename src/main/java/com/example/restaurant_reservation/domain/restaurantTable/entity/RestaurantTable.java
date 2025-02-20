package com.example.restaurant_reservation.domain.restaurantTable.entity;

import com.example.restaurant_reservation.domain.TimeStamp;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableResponseDto;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name ="restaurant_table")
@Entity
public class RestaurantTable extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_table_id", nullable = false)
    private Long id;

    @Column(name = "table_number", nullable = false, unique = true)
    private String tableNumber;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "is_available",nullable = false)
    private boolean isAvailable;

    @Builder
    public RestaurantTable(String tableNumber, int capacity, boolean isAvailable) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }

    public RestaurantTableResponseDto toResponseDto(){
        return RestaurantTableResponseDto.builder()
                .tableNumber(this.tableNumber)
                .capacity(this.capacity)
                .isAvailable(this.isAvailable)
                .build();
    }

    public void  updateInfo(String tableNumber, int capacity, boolean isAvailable) {
        if (tableNumber != null) this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }

    public void reserve(){
        if (!this.isAvailable){
            throw new IllegalStateException("こーのテーブルはすでに予約されています。");
        }
        this.isAvailable = false;
    }


}
