package com.example.restaurant_reservation.domain.user.dto;

import com.example.restaurant_reservation.domain.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class UserRequestDto {


    @NotBlank(message = "usernameがありません。")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "パスワードは8～20文字で、数字、大文字、小文字、特殊文字を含める必要があります。")
    private String password;

    @NotBlank(message = "お名前を入力してください。")
    private String name;

    @NotBlank @Email
    private String email;

    @NotBlank(message = "電話番号を入力してください。")
    @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$", message = "有効な電話番号を入力してください。")
    private String phone;

    @NotBlank
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
