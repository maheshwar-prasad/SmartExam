package com.smartexam.payment.config;

import com.razorpay.RazorpayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RazorpayProperties.class)
public class RazorpayConfig {

    private final RazorpayProperties properties;

    public RazorpayConfig(RazorpayProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        return new RazorpayClient(
                properties.keyId(),
                properties.keySecret()
        );
    }
}