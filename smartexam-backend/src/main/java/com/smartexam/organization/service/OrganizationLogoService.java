package com.smartexam.organization.service;

import com.smartexam.organization.dto.OrganizationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface OrganizationLogoService {

    OrganizationResponse uploadLogo(
            Long organizationId,
            MultipartFile file
    );

    LogoFile getLogo(
            Long organizationId
    );

    void deleteLogo(
            Long organizationId
    );


    record LogoFile(
            byte[] content,
            String contentType,
            String fileName
    ) {
    }
}