package com.example.restaurant_reservation.domain.restaurantTable.service;

import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableRequestDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableResponseDto;
import com.example.restaurant_reservation.domain.restaurantTable.dto.RestaurantTableUpdateDto;
import com.example.restaurant_reservation.domain.restaurantTable.entity.RestaurantTable;
import com.example.restaurant_reservation.domain.restaurantTable.repository.RestaurantTableRepository;
import jakarta.persistence.Table;
import org.hibernate.sql.ast.tree.expression.CaseSimpleExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.FilteredClassLoader;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantTableServiceImplTest {

    @Mock private RestaurantTableRepository restaurantTableRepository;

    @InjectMocks private RestaurantTableServiceImpl restaurantTableService;

    RestaurantTableRequestDto tableRequestDto;

    RestaurantTable table;

    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity,id);
    }

    @BeforeEach
    void setup() throws Exception {

        table = new RestaurantTable(
                "1번",
                10,
                true
        );
        setId(table,1L);

        tableRequestDto = new RestaurantTableRequestDto(
                this.table.getTableNumber(),
                this.table.getCapacity(),
                true
        );
    }

    @Test
    void createTable() {

        //given
        when(restaurantTableRepository.save(any(RestaurantTable.class))).thenReturn(table);

        //when
        RestaurantTableResponseDto result = restaurantTableService.createTable(tableRequestDto);


        //then
        assertNotNull(result);
        assertEquals("1번",result.getTableNumber());
        assertEquals(10,result.getCapacity());

        verify(restaurantTableRepository,times(1)).save(any(RestaurantTable.class));

    }

    @Test
    void findByTableId() {

        //given
        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.ofNullable(table));

        //when

        RestaurantTableResponseDto result = restaurantTableService.findByTableId(1L);

        //then
        assertNotNull(result);
        assertEquals(10, result.getCapacity());

        verify(restaurantTableRepository, times(1)).findById(1L);
    }

    @Test
    void updateTable() {
        //given
        RestaurantTableUpdateDto updatedto = RestaurantTableUpdateDto.builder()
                .tableNumber("2번")
                .capacity(5)
                .isAvailable(false).build();

        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.ofNullable(table));

        //when
        RestaurantTableResponseDto result = restaurantTableService.updateTable(1L, updatedto);

        //then
        assertNotNull(result);
        assertEquals("2번", result.getTableNumber());
        assertEquals(false, result.isAvailable());
    }

    @Test
    void delete() {
        //given
        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.ofNullable(table));

        //when
        restaurantTableService.delete(1L);
        //then

        verify(restaurantTableRepository, times(1)).delete(table);

    }
}