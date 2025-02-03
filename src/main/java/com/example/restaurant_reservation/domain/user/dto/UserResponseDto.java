package com.example.restaurant_reservation.domain.user.dto;

import com.example.restaurant_reservation.domain.user.entity.UserRole;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;

    @Builder
    public UserResponseDto(Long id, String username, String password, String name, String email, String phone, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    @QueryProjection
    public UserResponseDto(Long id, String username, String password, String name, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
