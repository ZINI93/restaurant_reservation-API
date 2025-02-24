package com.example.restaurant_reservation.config;

import com.example.restaurant_reservation.domain.user.entity.UserRole;
import com.example.restaurant_reservation.domain.user.service.CustomUserDetailsService;
import com.example.restaurant_reservation.security.CustomOAuth2UserService;
import com.example.restaurant_reservation.security.JwtRequestFilter;
import com.example.restaurant_reservation.security.OAuth2LoginFailureHandler;
import com.example.restaurant_reservation.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.awt.desktop.PrintFilesEvent;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private final JwtRequestFilter jwtRequestFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {

        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role(UserRole.ADMIN.toString()).implies(UserRole.USER.toString())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrfConfig) -> csrfConfig.ignoringRequestMatchers("/api/**", "/authenticate"))  // ✅ CSRF 예외 추가
                .headers((headerConfig) ->
                        headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
                )
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/api/join").permitAll()
                        .requestMatchers("/api/admin/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/api/**").hasRole(UserRole.USER.name())
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)// OAuth2 사용자 정보 처리
                        )
                        .successHandler(oAuth2LoginSuccessHandler)  // 로그인 성공 후 처리
                        .failureHandler(oAuth2LoginFailureHandler)  // 실패 핸들러 추가
                        .permitAll() // OAuth2 로그인 경로는 모두 허용

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 기반으로 stateless 설정
                        .sessionFixation().migrateSession()
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}







