package com.smartexam.auth.controller;

import com.smartexam.auth.dto.*;
import com.smartexam.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiMessage> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiMessage> logout() {

        return ResponseEntity.ok(
                authenticationService.logout()
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiMessage> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        return ResponseEntity.ok(
                authenticationService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiMessage> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(
                authenticationService.resetPassword(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiMessage> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                authenticationService.changePassword(request));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiMessage> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        return ResponseEntity.ok(
                authenticationService.verifyOtp(request)
        );
    }
}