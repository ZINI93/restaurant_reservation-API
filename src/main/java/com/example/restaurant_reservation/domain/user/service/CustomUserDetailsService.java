package com.example.restaurant_reservation.domain.user.service;

import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("로그인 시도: " + username);
        return userRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("DB에서 찾은 사용자: " + user.getUsername());
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> {
                    System.out.println("사용자를 찾을 수 없음: " + username);
                    return new UsernameNotFoundException("User not found");
                });
    }
}