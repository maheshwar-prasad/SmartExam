package com.smartexam.auth.service;

public interface EmailService {

    void sendPasswordResetOtp(
            String email,
            String otp
    );
}