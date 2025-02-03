package com.example.restaurant_reservation.domain.restaurantTable.entity;

import com.example.restaurant_reservation.domain.TimeStamp;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableResponseDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTable extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RestaurantTable_id", nullable = false)
    private Long id;

    private String tableNumber;

    private String capacity;

    private boolean isAvailable;

    @Builder
    public RestaurantTable(String tableNumber, String capacity, boolean isAvailable) {
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

    public void  updateInfo(String tableNumber, String capacity, boolean isAvailable) {
        if (tableNumber != null) this.tableNumber = tableNumber;
        if (capacity != null)this.capacity = capacity;
        this.isAvailable = isAvailable;
    }





}
