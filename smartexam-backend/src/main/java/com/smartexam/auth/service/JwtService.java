package com.smartexam.auth.service;

import com.smartexam.user.entity.User;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractUsername(String token);

    boolean validateToken(String token);

    boolean isTokenExpired(String token);

}