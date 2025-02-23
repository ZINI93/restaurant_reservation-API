package com.example.restaurant_reservation.util;

import com.example.restaurant_reservation.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    JwtUtil jwtUtil;
    private SecretKey secretKey;
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        //given
        String username = "testUser";

        //when
        String token = jwtUtil.generateToken(username);


        //then
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void validateToken_test() {
        // Given
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        //When
        boolean isValid = jwtUtil.validateToken(token, username);

        //then
        assertTrue(isValid);

    }

    @Test
    void validateToken_test2() {
        //given
        String validUsername = "testUser";
        String invalidUsername = "invalidUser";
        String token = jwtUtil.generateToken(validUsername);


        //when
        boolean isValid = jwtUtil.validateToken(token, invalidUsername);

        //then
        assertFalse(isValid);
    }

    @Test
    void ExpiredToken() throws InterruptedException {
        //given
        String username = "testUser";

        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();

        //when
        boolean isValid = jwtUtil.validateToken(expiredToken, username);

        //then
        assertFalse(isValid);
    }

}