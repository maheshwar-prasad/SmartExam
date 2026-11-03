package com.smartexam.auth.service;

import com.smartexam.auth.dto.*;

public interface AuthenticationService {

    JwtResponse login(LoginRequest request);

    ApiMessage register(RegisterRequest request);

    JwtResponse refreshToken(RefreshTokenRequest request);

    ApiMessage logout();

    ApiMessage forgotPassword(ForgotPasswordRequest request);

    ApiMessage resetPassword(ResetPasswordRequest request);

    ApiMessage changePassword(ChangePasswordRequest request);

    ApiMessage verifyOtp(VerifyOtpRequest request);

}