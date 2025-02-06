package com.example.restaurant_reservation.domain.user.service;

import com.example.restaurant_reservation.domain.user.dto.UserRequestDto;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDto createUser(UserRequestDto RequestDto);

    UserResponseDto findById(Long userId);

    Page<UserResponseDto> SearchUser(String username, String name , String phone, Pageable pageable);

    UserResponseDto updateUser(Long userId, UserUpdateDto updateDto);

    void deleteUser(Long userId);

}
