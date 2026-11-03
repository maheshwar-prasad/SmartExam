package com.smartexam.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationUpdateRequest {

    @NotBlank(message = "Organization name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Organization code is required")
    @Size(max = 30)
    private String code;

    @NotBlank(message = "Organization email is required")
    @Email(message = "Invalid organization email")
    private String email;

    @Size(max = 20)
    private String mobile;

    @Size(max = 300)
    private String address;
}