package com.example.restaurant_reservation.domain.restaurantTable.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RestaurantTableResponseDto {

    private Long id;
    private String tableNumber;
    private int capacity;
    private boolean isAvailable;


    @Builder
    public RestaurantTableResponseDto(String tableNumber, int capacity, boolean isAvailable) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }
}
