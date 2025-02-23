package com.example.restaurant_reservation.security;

import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.entity.UserRole;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DefaultOAuth2UserService defaultOAuth2UserService;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;


    @DisplayName("OAuth2ログイン, ユーザーの情報を保存をテースト")
    @Test
    void OAuth_login_saveTest() {

        //Given
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2User mockOAuth2User = mock(OAuth2User.class);

        when(defaultOAuth2UserService.loadUser(userRequest)).thenReturn(mockOAuth2User);
        when(mockOAuth2User.getAttribute("email")).thenReturn("test@gmail.com");
        when(mockOAuth2User.getAttributes()).thenReturn(Map.of("name", "name"));

        User user = User.builder()
                .email("test@gmail.com")
                .name("name")
                .role(UserRole.USER)
                .userUuid(UUID.randomUUID().toString())
                .provider("google")
                .build();

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        //WHEN
        OAuth2User result = customOAuth2UserService.loadUser(userRequest);

        //THEN
        assertNotNull(result);
        assertEquals("test@gmail.com",result.getName());
    }
}