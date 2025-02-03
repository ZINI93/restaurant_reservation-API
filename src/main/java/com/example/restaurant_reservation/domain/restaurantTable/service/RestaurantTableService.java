package com.example.restaurant_reservation.domain.restaurantTable.service;

import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableRequestDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableResponseDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableUpdateDto;

public interface RestaurantTableService {



    RestaurantTableResponseDto createTable(RestaurantTableRequestDto requestDto);
    RestaurantTableResponseDto findByTableId(Long restaurantTableId);
    RestaurantTableResponseDto updateTable(Long restaurantTableId, RestaurantTableUpdateDto updateDto);
    void delete(Long restaurantTableId);
}
