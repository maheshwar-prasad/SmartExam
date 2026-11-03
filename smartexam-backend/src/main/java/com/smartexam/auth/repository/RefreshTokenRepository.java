package com.smartexam.auth.repository;

import com.smartexam.auth.entity.RefreshToken;
import com.smartexam.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    boolean existsByToken(String token);

    @Transactional
    @Modifying
    void deleteByUser(User user);
}