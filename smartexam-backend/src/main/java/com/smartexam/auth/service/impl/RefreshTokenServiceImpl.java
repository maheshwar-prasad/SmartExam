package com.smartexam.auth.service.impl;

import com.smartexam.auth.entity.RefreshToken;
import com.smartexam.auth.repository.RefreshTokenRepository;
import com.smartexam.auth.service.RefreshTokenService;
import com.smartexam.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = repository.findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );
        refreshToken.setRevoked(false);
        return repository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {

        return repository.findByToken(token);

    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {

            repository.delete(token);

            throw new RuntimeException("Refresh Token Expired");

        }

        return token;

    }

    @Override
    public void deleteByUser(User user) {

        repository.deleteByUser(user);

    }

    @Override
    public void revokeToken(String token) {

        RefreshToken refreshToken =
                repository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Token not found"));
        refreshToken.setRevoked(true);
        repository.save(refreshToken);

    }
}