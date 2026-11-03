package com.smartexam.institution.settings.controller;

import com.smartexam.institution.settings.dto.FileUploadResponse;
import com.smartexam.institution.settings.service.InstitutionLogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/institution-logos")
public class InstitutionLogoController {

    private final InstitutionLogoService institutionLogoService;

    public InstitutionLogoController(
            InstitutionLogoService institutionLogoService
    ) {
        this.institutionLogoService = institutionLogoService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FileUploadResponse> uploadLogo(
            @RequestParam("organizationId") Long organizationId,
            @RequestParam("file") MultipartFile file
    ) {
        FileUploadResponse response =
                institutionLogoService.uploadLogo(
                        organizationId,
                        file
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLogo(
            @RequestParam("logoUrl") String logoUrl
    ) {
        institutionLogoService.deleteLogo(logoUrl);
        return ResponseEntity.noContent().build();
    }
}