package com.example.restaurant_reservation.domain.user.dto;

import com.example.restaurant_reservation.domain.user.entity.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
public class UserUpdateDto {

    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;


    @Builder
    public UserUpdateDto(String password, String name, String email, String phone, UserRole role) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
