package com.example.restaurant_reservation.domain.restaurantTable.service;

import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableRequestDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableResponseDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableUpdateDto;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.restaurantTable.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RestaurantTableServiceImpl implements RestaurantTableService{

    private final RestaurantTableRepository restaurantTableRepository;


    // テーブルを作る
    @Override  @Transactional
    public RestaurantTableResponseDto createTable(RestaurantTableRequestDto requestDto) {

        RestaurantTable saved = RestaurantTable.builder()
                .tableNumber(requestDto.getTableNumber())
                .capacity(requestDto.getCapacity())
                .isAvailable(true)
                .build();

        return restaurantTableRepository.save(saved).toResponseDto();
    }

    //　テーブルidで検索
    @Override
    public RestaurantTableResponseDto findByTableId(Long restaurantTableId) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(restaurantTableId)
                .orElseThrow(() -> new IllegalArgumentException("テーブルIDが見つかりません"));
        return restaurantTable.toResponseDto();
    }

    //　テーブルをアップデート
    @Override  @Transactional
    public RestaurantTableResponseDto updateTable(Long restaurantTableId,  RestaurantTableUpdateDto updateDto) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(restaurantTableId)
                .orElseThrow(() -> new IllegalArgumentException("テーブルIDが見つかりません"));

        restaurantTable.updateInfo(updateDto.getTableNumber(),
                updateDto.getCapacity(),
                updateDto.isAvailable());

        return restaurantTable.toResponseDto();
    }

    //　テーブルを削除
    @Override @Transactional
    public void delete(Long restaurantTableId) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(restaurantTableId)
                .orElseThrow(() -> new IllegalArgumentException("テーブルIDが見つかりません"));

        restaurantTableRepository.delete(restaurantTable);
    }
}
