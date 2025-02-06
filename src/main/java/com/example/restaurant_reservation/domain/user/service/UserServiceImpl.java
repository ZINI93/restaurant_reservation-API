package com.example.restaurant_reservation.domain.user.service;

import com.example.restaurant_reservation.domain.user.dto.UserRequestDto;
import com.example.restaurant_reservation.domain.user.dto.UserResponseDto;
import com.example.restaurant_reservation.domain.user.dto.UserUpdateDto;
import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.entity.UserRole;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.PosixFileAttributes;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // アカウント作成
    @Override @Transactional
    public UserResponseDto createUser(UserRequestDto RequestDto) {
        String encodePassword = passwordEncoder.encode(RequestDto.getPassword());

        User savedUser = User.builder()
                .username(RequestDto.getUsername())
                .password(encodePassword)
                .name(RequestDto.getName())
                .email(RequestDto.getEmail())
                .phone(RequestDto.getPhone())
                .role(UserRole.USER)
                .build();

        return userRepository.save(savedUser).toResponse();
    }

    //　ユーザーIDで検索
    @Override
    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーのＩＤが見つかりません。"));
        return user.toResponse();

    }

    //　ユーザー（ID, お名前、電話番号)で検索
    @Override
    public Page<UserResponseDto> SearchUser(String username, String name, String phone, Pageable pageable) {
        return userRepository.searchUser(username,name,phone,pageable);
    }

    //ユーザー情報をアップデート
    @Override @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーのＩＤが見つかりません。"));

        //  비밀번호 변경하지 않을때 기존 값 유지
        String encodedPassword = updateDto.getPassword() != null
                ? passwordEncoder.encode(updateDto.getPassword())
                : user.getPassword();

        user.updateInfo(encodedPassword,
                updateDto.getName(),
                updateDto.getEmail(),
                updateDto.getPhone(),
                updateDto.getRole());

        return user.toResponse();

    }

    //　ユーザーを削除
    @Override @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーのＩＤが見つかりません。"));

        userRepository.delete(user);
    }
}

