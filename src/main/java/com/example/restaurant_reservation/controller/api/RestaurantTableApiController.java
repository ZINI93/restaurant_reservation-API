package com.example.restaurant_reservation.controller.api;


import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableRequestDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableResponseDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableUpdateDto;
import com.example.restaurant_reservation.domain.restaurantTable.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/admin/restaurant_table")
@RestController
public class RestaurantTableApiController {

    private final RestaurantTableService tableService;

    @PostMapping
    public ResponseEntity<RestaurantTableResponseDto> createRestaurantTable(@RequestBody RestaurantTableRequestDto requestDto){

        RestaurantTableResponseDto table = tableService.createTable(requestDto);
        URI location = URI.create("/api/restaurant_table/" + table.getId());

        return ResponseEntity.created(location).body(table);
    }

    @GetMapping("{tableId}")
    public ResponseEntity<RestaurantTableResponseDto> findByTable(@PathVariable Long tableId){

        RestaurantTableResponseDto table = tableService.findByTableId(tableId);

        return ResponseEntity.ok(table);

    }

    @PutMapping("{tableId}")
    public ResponseEntity<RestaurantTableResponseDto> updateTable(@PathVariable Long tableId,
                                                                  @RequestBody RestaurantTableUpdateDto updateDto){

        RestaurantTableResponseDto table = tableService.updateTable(tableId, updateDto);

        return ResponseEntity.ok(table);
    }

    @DeleteMapping("{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long tableId){

        tableService.delete(tableId);

        return ResponseEntity.noContent().build();
    }


}
