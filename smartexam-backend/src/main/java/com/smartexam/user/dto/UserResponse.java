package com.smartexam.user.dto;

import com.smartexam.common.enums.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private Gender gender;

    private String organizationName;

    private String roleName;

    private Boolean active;

    private Boolean enabled;

    private LocalDateTime createdAt;

}