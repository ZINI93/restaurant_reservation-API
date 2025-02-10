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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("로그인 시도: " + email);
        return userRepository.findByUsername(email)
                .map(member -> {
                    System.out.println("DB에서 찾은 사용자: " + member.getEmail());
                    return new UserPrincipal(member.getEmail(), member.getPassword());
                })
                .orElseThrow(() -> {
                    System.out.println("사용자를 찾을 수 없음: " + email);
                    return new UsernameNotFoundException("User not found");
                });
    }
}