package com.example.restaurant_reservation.controller;

import com.example.restaurant_reservation.util.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public Map<String, String> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
        }catch (AuthenticationException e){
            throw  new RuntimeException("Invalid credentials");
        }

        final String jwt = jwtUtil.generateToken(authRequest.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("jwt", jwt);
        return response;
    }
}

@Getter
@Setter
class AuthRequest{

    private String username;
    private String password;

}

