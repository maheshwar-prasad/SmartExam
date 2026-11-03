package com.smartexam.parentguardian.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentGuardianResponse {

    private Long id;

    private Long organizationId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String mobile;

    private String alternateMobile;

    private String email;

    private String occupation;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private Boolean active;

    private String createdAt;

    private String updatedAt;
}