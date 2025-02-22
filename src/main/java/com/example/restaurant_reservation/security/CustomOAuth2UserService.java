package com.example.restaurant_reservation.security;

import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.entity.UserRole;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final UserRepository userRepository;
    private final DefaultOAuth2UserService defaultOAuth2UserService;

    public CustomOAuth2UserService(DefaultOAuth2UserService defaultOAuth2UserService, UserRepository userRepository) {
        this.defaultOAuth2UserService = defaultOAuth2UserService;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        // DefaultOAuth2UserService를 사용해 OAuth2User 로드
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        // Google에서 제공한 사용자 정보 추출
        String email = oAuth2User.getAttribute("email");
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // DB에서 사용자 조회 또는 생성
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    //새로운 유저 생성
                    User newUser = User.builder()
                            .username(email)
                            .email(email)
                            .name(oAuth2User.getAttribute("name"))
                            .password(null)
                            .role(UserRole.USER)
                            .userUuid(UUID.randomUUID().toString())
                            .provider("google")
                            .build();
                    return userRepository.save(newUser);
                });

        // CustomOAuth2User 반환
        return new CustomOAuth2User(user,attributes);
    }
}
