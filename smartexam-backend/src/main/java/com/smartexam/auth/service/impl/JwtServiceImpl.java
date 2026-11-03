package com.smartexam.auth.service.impl;

import com.smartexam.auth.service.JwtService;
import com.smartexam.security.JwtUtil;
import com.smartexam.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;

    @Override
    public String generateAccessToken(User user) {

        return jwtUtil.generateAccessToken(
                user.getEmail()
        );

    }

    @Override
    public String generateRefreshToken(User user) {

        return jwtUtil.generateRefreshToken(
                user.getEmail()
        );

    }

    @Override
    public String extractUsername(String token) {

        return jwtUtil.extractUsername(token);

    }

    @Override
    public boolean validateToken(String token) {

        return !jwtUtil.isTokenExpired(token);

    }

    @Override
    public boolean isTokenExpired(String token) {

        return jwtUtil.isTokenExpired(token);

    }

}