package com.smartexam.auth.service.impl;

import javax.management.relation.RoleNotFoundException;
import com.smartexam.auth.dto.*;
import com.smartexam.auth.entity.PasswordResetOtp;
import com.smartexam.auth.entity.RefreshToken;
import com.smartexam.auth.repository.PasswordResetOtpRepository;
import com.smartexam.auth.repository.RefreshTokenRepository;
import com.smartexam.auth.service.AuthenticationService;
import com.smartexam.auth.service.EmailService;
import com.smartexam.auth.service.JwtService;
import com.smartexam.auth.service.RefreshTokenService;
import com.smartexam.common.enums.RoleName;
import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.common.exception.UnauthorizedException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.role.entity.Role;
import com.smartexam.role.repository.RoleRepository;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final EmailService emailService;
    // Remaining methods will be added in later parts

    @Override
    public JwtResponse login(LoginRequest request) {

        log.info("Login request received for {}", request.getEmail());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        log.info("User {} logged in successfully", user.getEmail());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .username(user.getEmail())
                .role(user.getRole().getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }

    @SneakyThrows
    @Override
    @Transactional
    public ApiMessage register(RegisterRequest request) {

        log.info("Register request received for {}", request.getEmail());
        // Validate password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password do not match.");
        }
        // Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        // Check mobile
        if (userRepository.existsByMobile(request.getMobile())) {
            throw new IllegalArgumentException("Mobile number already exists.");
        }
        // Find organization
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        // Find role
        Role role = roleRepository.findByName(RoleName.STUDENT.name())
                .orElseThrow(() -> new RoleNotFoundException("Default role not found"));
        // Create user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .organization(organization)
                .role(role)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .active(true)
                .build();

        userRepository.save(user);
        log.info("User {} registered successfully.", user.getEmail());
        return ApiMessage.builder()
                .success(true)
                .message("Registration completed successfully.")
                .build();
    }
    @Override
    @Transactional
    public JwtResponse refreshToken(RefreshTokenRequest request) {

        log.info("Refresh token request received.");
        RefreshToken refreshToken = refreshTokenService
                .findByToken(request.getRefreshToken())
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found."));
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateAccessToken(user);
        /*
         * Token Rotation
         * Revoke old refresh token and create a new one
         */
        refreshTokenService.deleteByUser(user);
        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(user);
        log.info("Access token refreshed successfully for {}", user.getEmail());
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .username(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    @Transactional
    public ApiMessage logout() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new UnauthorizedException(
                    "User is not authenticated"
            );
        }

        String email = authentication
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                email
                        )
                );

        refreshTokenRepository.deleteByUser(user);

        SecurityContextHolder.clearContext();

        return ApiMessage.builder()
                .success(true)
                .message("Logged out successfully")
                .build();
    }
    @Override
    @Transactional
    public ApiMessage forgotPassword(
            ForgotPasswordRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                request.getEmail()
                        )
                );

        // Invalidate previous OTPs
        passwordResetOtpRepository
                .findTopByUserAndUsedFalseOrderByCreatedAtDesc(user)
                .ifPresent(previousOtp -> {
                    previousOtp.setUsed(true);
                    passwordResetOtpRepository.save(previousOtp);
                });

        // Generate 6-digit OTP
        String otp = String.format(
                "%06d",
                new java.security.SecureRandom()
                        .nextInt(1_000_000)
        );

        PasswordResetOtp passwordResetOtp =
                PasswordResetOtp.builder()
                        .user(user)
                        .otpHash(
                                passwordEncoder.encode(otp)
                        )
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusMinutes(5)
                        )
                        .verified(false)
                        .used(false)
                        .attempts(0)
                        .build();

        passwordResetOtpRepository.save(
                passwordResetOtp
        );
        // Send OTP through email
        emailService.sendPasswordResetOtp(
                user.getEmail(),
                otp
        );
        // Temporary development logging only
        log.info(
                "Password reset OTP generated for user: {}",
                user.getEmail()
        );

        return ApiMessage.builder()
                .success(true)
                .message("OTP generated successfully")
                .build();
    }
    @Override
    @Transactional
    public ApiMessage resetPassword(
            ResetPasswordRequest request) {

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new IllegalArgumentException(
                    "New password and confirm password do not match"
            );
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                request.getEmail()
                        )
                );

        PasswordResetOtp resetOtp =
                passwordResetOtpRepository
                        .findTopByUserAndUsedFalseOrderByCreatedAtDesc(user)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Please verify OTP before resetting password"
                                )
                        );

        if (!resetOtp.isVerified()) {

            throw new IllegalArgumentException(
                    "Please verify OTP before resetting password"
            );
        }

        if (resetOtp.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            resetOtp.setUsed(true);
            passwordResetOtpRepository.save(resetOtp);

            throw new IllegalArgumentException(
                    "OTP has expired"
            );
        }

        // Prevent reusing the current password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }

        // Update password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        // Consume OTP
        resetOtp.setUsed(true);
        passwordResetOtpRepository.save(resetOtp);

        // Revoke existing refresh tokens
        refreshTokenRepository.deleteByUser(user);

        return ApiMessage.builder()
                .success(true)
                .message("Password reset successfully")
                .build();
    }

    @Override
    @Transactional
    public ApiMessage changePassword(
            ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new UnauthorizedException(
                    "User is not authenticated"
            );
        }

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new BusinessException(
                    "Current password is incorrect"
            );
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new BusinessException(
                    "New password and confirm password do not match"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new BusinessException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        // Existing repository method
        refreshTokenRepository.deleteByUser(user);

        return ApiMessage.builder()
                .message("Password changed successfully")
                .build();
    }


    @Override
    @Transactional
    public ApiMessage verifyOtp(
            VerifyOtpRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                request.getEmail()
                        )
                );

        PasswordResetOtp resetOtp =
                passwordResetOtpRepository
                        .findTopByUserAndUsedFalseOrderByCreatedAtDesc(user)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid or expired OTP"
                                )
                        );

        // Check expiry
        if (resetOtp.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            resetOtp.setUsed(true);
            passwordResetOtpRepository.save(resetOtp);

            throw new IllegalArgumentException(
                    "OTP has expired"
            );
        }

        // Maximum attempts
        if (resetOtp.getAttempts() >= 5) {

            resetOtp.setUsed(true);
            passwordResetOtpRepository.save(resetOtp);

            throw new IllegalArgumentException(
                    "Maximum OTP verification attempts exceeded"
            );
        }

        // Verify OTP
        if (!passwordEncoder.matches(
                request.getOtp(),
                resetOtp.getOtpHash())) {

            resetOtp.setAttempts(
                    resetOtp.getAttempts() + 1
            );

            passwordResetOtpRepository.save(resetOtp);

            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }

        // OTP is valid
        resetOtp.setVerified(true);

        passwordResetOtpRepository.save(resetOtp);

        return ApiMessage.builder()
                .success(true)
                .message("OTP verified successfully")
                .build();
    }
}