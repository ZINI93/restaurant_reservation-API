package com.example.restaurant_reservation.domain.restaurantTable.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RestaurantTableRequestDto {


    private String tableNumber;

    private String capacity;

    private boolean isAvailable;

    @Builder
    public RestaurantTableRequestDto(String tableNumber, String capacity, boolean isAvailable) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }
}
