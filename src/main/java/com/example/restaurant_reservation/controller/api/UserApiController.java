package com.example.restaurant_reservation.controller.api;

import com.example.restaurant_reservation.domain.user.dto.UserRequestDto;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.dto.UserUpdateDto;
import com.example.restaurant_reservation.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserApiController {

    private final UserService userService;

    // アカウント作成
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto){
        UserResponseDto user = userService.createUser(requestDto);
        URI location = URI.create("/api/user/" + user.getId());

        return ResponseEntity.created(location).body(user);
    }


    @GetMapping("{userId}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long userId){
        UserResponseDto user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> searchUser(@RequestParam(required = false) String username,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false)String phone,
                                                            @RequestParam(required = false) Pageable pageable){
        Page<UserResponseDto> user = userService.SearchUser(username, name, phone, pageable);

        return ResponseEntity.ok(user);
    }


    @PutMapping("{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @RequestBody UserUpdateDto updateDto){

        UserResponseDto user = userService.updateUser(userId, updateDto);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userid){

        userService.deleteUser(userid);

        return ResponseEntity.noContent().build();
    }




}
