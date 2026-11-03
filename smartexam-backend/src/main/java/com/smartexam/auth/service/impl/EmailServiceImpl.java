package com.smartexam.auth.service.impl;

import com.smartexam.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetOtp(
            String email,
            String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(
                "SmartExam - Password Reset OTP"
        );

        message.setText("""
                Hello,

                Your SmartExam password reset OTP is:
                %s
                This OTP is valid for 5 minutes.
                If you did not request a password reset,
                please ignore this email.
                Regards,
                SmartExam Team
                """.formatted(otp));
        mailSender.send(message);
        log.info(
                "Password reset OTP email sent to {}",
                email
        );
    }
}