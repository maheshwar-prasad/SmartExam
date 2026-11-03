package com.smartexam.organization.controller;

import com.smartexam.organization.dto.OrganizationResponse;
import com.smartexam.organization.service.OrganizationLogoService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Validated
public class OrganizationLogoController {


    private final OrganizationLogoService organizationLogoService;


    // =========================================================
    // UPLOAD / CHANGE LOGO
    // =========================================================

    @PostMapping(
            value = "/{id}/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<@NotNull OrganizationResponse> uploadLogo(
            @PathVariable("id") Long id,
            @RequestPart("file") MultipartFile file) {

        OrganizationResponse response =
                organizationLogoService.uploadLogo(
                        id,
                        file
                );

        return ResponseEntity.ok(
                response
        );
    }


    // =========================================================
    // GET / DOWNLOAD LOGO
    // =========================================================

    @GetMapping("/{id}/logo")
    public ResponseEntity<ByteArrayResource> getLogo(
            @PathVariable("id") Long id) {

        OrganizationLogoService.LogoFile logo =
                organizationLogoService.getLogo(id);


        ByteArrayResource resource =
                new ByteArrayResource(
                        logo.content()
                );


        MediaType mediaType;

        try {

            mediaType =
                    MediaType.parseMediaType(
                            logo.contentType()
                    );

        } catch (Exception exception) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }


        HttpHeaders headers =
                new HttpHeaders();


        headers.setContentType(
                mediaType
        );


        headers.setContentLength(
                logo.content().length
        );


        headers.setContentDisposition(
                ContentDisposition
                        .inline()
                        .filename(
                                logo.fileName()
                        )
                        .build()
        );


        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


    // =========================================================
    // DELETE LOGO
    // =========================================================

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteLogo(
            @PathVariable("id") Long id) {

        organizationLogoService.deleteLogo(
                id
        );
        return ResponseEntity.noContent()
                .build();
    }
}