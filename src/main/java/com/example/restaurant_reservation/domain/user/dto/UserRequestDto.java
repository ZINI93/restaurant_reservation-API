package com.example.restaurant_reservation.domain.user.dto;

import com.example.restaurant_reservation.domain.user.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class UserRequestDto {

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;

    @Builder
    public UserRequestDto(String username, String password, String name, String email, String phone, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
