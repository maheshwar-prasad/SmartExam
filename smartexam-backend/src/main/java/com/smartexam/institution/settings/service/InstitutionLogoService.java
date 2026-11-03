package com.smartexam.institution.settings.service;

import com.smartexam.institution.settings.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface InstitutionLogoService {

    FileUploadResponse uploadLogo(
            Long organizationId,
            MultipartFile file
    );

    void deleteLogo(String logoUrl);
}