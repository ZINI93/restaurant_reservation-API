package com.example.restaurant_reservation.domain.restaurantTable.repository;

import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}