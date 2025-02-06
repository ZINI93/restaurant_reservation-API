package com.example.restaurant_reservation.domain.user.repository;

import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    // username, name, phone
    Page<UserResponseDto> searchUser(String username, String name , String phone, Pageable pageable);
}
