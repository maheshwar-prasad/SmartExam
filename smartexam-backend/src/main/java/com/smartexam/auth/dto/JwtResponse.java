package com.smartexam.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private String username;

    private String role;

    private String firstName;

    private String lastName;

}