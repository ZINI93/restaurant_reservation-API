package com.example.restaurant_reservation.domain.restaurantTable.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RestaurantTableResponseDto {

    private Long id;
    private String tableNumber;
    private String capacity;
    private boolean isAvailable;


    @Builder
    public RestaurantTableResponseDto(String tableNumber, String capacity, boolean isAvailable) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }
}
