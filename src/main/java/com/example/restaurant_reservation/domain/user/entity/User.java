package com.example.restaurant_reservation.domain.user.entity;

import com.example.restaurant_reservation.domain.TimeStamp;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(String username, String password, String name, String email, String phone, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public UserResponseDto toResponse(){
        return UserResponseDto
                .builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .role(this.role)
                .build();
    }

    public void updatePassword(String rawPassword, BCryptPasswordEncoder passwordEncoder){
        this.password =passwordEncoder.encode(rawPassword);
    }

    public void updateInfo(String password, String name, String email, String phone, UserRole role) {
        if (password != null) this.password = password;
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (phone != null) this.phone = phone;
        if (role != null) this.role = role;
    }
}
