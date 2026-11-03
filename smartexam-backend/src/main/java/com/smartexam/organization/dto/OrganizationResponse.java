package com.smartexam.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrganizationResponse {

    private Long id;

    private String name;

    private String code;

    private String email;

    private String mobile;

    private String address;

    private Boolean active;

    private String logoPath;
    private String logoContentType;
    private String logoOriginalFileName;
}