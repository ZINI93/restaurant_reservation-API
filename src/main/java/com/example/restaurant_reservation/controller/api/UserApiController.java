package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.user.dto.UserRequestDto;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.dto.UserUpdateDto;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetails;
import com.example.restaurant_reservation.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApiController {

    private final UserService userService;

    // アカウント作成 .
    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto){
        UserResponseDto user = userService.createUser(requestDto);
        URI location = URI.create("/api/user/" + user.getId());

        return ResponseEntity.created(location).body(user);
    }


    // ユーザーIDで検索
    @GetMapping("/user/me")
    public ResponseEntity<UserResponseDto> findById(Authentication authentication){
        // 本人以外のIDはアクセス不可
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        UserResponseDto user = userService.findById(userId);

        return ResponseEntity.ok(user);
    }

    // ユーザーをアップデート
    @PutMapping("/user/me")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateDto updateDto,
                                                      Authentication authentication) {
        // 本人以外のIDはアクセス不可
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();

        UserResponseDto user = userService.updateUser(userId, updateDto);

        return ResponseEntity.ok(user);
    }

}
