package com.example.restaurant_reservation.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);//トークンを作る

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + 1000 * 60 * 60 * 10))) //　トークン　有効時間　１０時間
                .signWith(SECRET_KEY)
                .compact();
    }

    // トークンからユーザーidをとる
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }


    //トークンから有効性を検索
    public boolean validateToken(String token, String username){
       try {
           final String extractUsername = extractUsername(token);
           return (username.equals(extractUsername) && !isTokenExpired(token)) ;
       }catch (JwtException | IllegalArgumentException e){
           return false; // 토큰이 변조되었거나 유효하지 않으면 false 반환
       }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
