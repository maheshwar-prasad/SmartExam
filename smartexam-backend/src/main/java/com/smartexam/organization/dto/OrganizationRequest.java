package com.smartexam.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequest {

    @NotBlank(message = "Organization name is required")
    @Size(max = 150, message = "Organization name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Organization code is required")
    @Size(max = 30, message = "Organization code must not exceed 30 characters")
    private String code;

    @NotBlank(message = "Organization email is required")
    @Email(message = "Invalid organization email")
    private String email;

    @Size(max = 20, message = "Mobile must not exceed 20 characters")
    private String mobile;

    @Size(max = 300, message = "Address must not exceed 300 characters")
    private String address;

    private String logoPath;

    private String logoContentType;

    private String logoOriginalFileName;

}