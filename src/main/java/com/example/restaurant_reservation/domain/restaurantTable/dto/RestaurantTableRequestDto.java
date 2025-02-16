package com.example.restaurant_reservation.domain.restaurantTable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class RestaurantTableRequestDto {


    @NotBlank(message = "テーブルを番号がありません。")
    private String tableNumber;

    @NotNull
    private int capacity;

    private boolean isAvailable;

    @Builder
    public RestaurantTableRequestDto(String tableNumber, int capacity, boolean isAvailable) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }
}
