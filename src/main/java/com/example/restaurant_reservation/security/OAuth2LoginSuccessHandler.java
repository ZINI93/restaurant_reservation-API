package com.example.restaurant_reservation.security;

import com.example.restaurant_reservation.domain.user.entity.User;
import com.example.restaurant_reservation.domain.user.repository.UserRepository;
import com.example.restaurant_reservation.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User에서 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        String email = oAuth2User.getName();


        // DB에서 사용자 정보 가져오기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーの情報を見つかりません。"));

        // JWT 생성
        String jwtToken = jwtUtil.generateToken(user.getEmail());

        System.out.println("jwtToken = " + jwtToken);

        // JWT를 프론트엔드에 반환
        response.setHeader("Authorization", "Bearer" + jwtToken);

        // JSON 응답 본문에 토큰 포함 (선택 사항)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");


        // 로그인 후 특정 URL로 리다이렉트 (필요하면 설정)
        response.sendRedirect("/dashboard");
    }
}
