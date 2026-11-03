package com.smartexam.organization.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.dto.OrganizationResponse;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.mapper.OrganizationMapper;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.organization.service.OrganizationLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationLogoServiceImpl
        implements OrganizationLogoService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;


    @Value("${smartexam.storage.organization-logos.location:./uploads/organization-logos}")
    private String storageLocation;


    private static final long MAX_FILE_SIZE =
            5 * 1024 * 1024;


    // =========================================================
    // UPLOAD LOGO
    // =========================================================

    @Override
    public OrganizationResponse uploadLogo(
            Long organizationId,
            MultipartFile file) {

        Organization organization =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        organizationId
                                )
                        );


        validateFile(file);


        try {

            Path organizationDirectory =
                    Paths.get(storageLocation)
                            .toAbsolutePath()
                            .normalize()
                            .resolve(
                                    String.valueOf(
                                            organizationId
                                    )
                            );

            Files.createDirectories(
                    organizationDirectory
            );


            // Remove old logo if present
            deleteExistingFile(
                    organization
            );


            String extension =
                    getExtension(
                            file.getOriginalFilename()
                    );


            String storedFileName =
                    UUID.randomUUID()
                            + extension;


            Path targetPath =
                    organizationDirectory
                            .resolve(storedFileName)
                            .normalize();


            // Security check
            if (!targetPath.startsWith(
                    organizationDirectory
            )) {

                throw new IllegalArgumentException(
                        "Invalid logo file path."
                );
            }


            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            String relativePath =
                    Paths.get(storageLocation)
                            .normalize()
                            .resolve(
                                    String.valueOf(
                                            organizationId
                                    )
                            )
                            .resolve(storedFileName)
                            .toString();


            organization.setLogoPath(
                    relativePath
            );

            organization.setLogoContentType(
                    file.getContentType()
            );

            organization.setLogoOriginalFileName(
                    file.getOriginalFilename()
            );


            Organization saved =
                    organizationRepository.save(
                            organization
                    );


            return organizationMapper.toResponse(
                    saved
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Unable to store organization logo.",
                    exception
            );
        }
    }


    // =========================================================
    // GET LOGO
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public LogoFile getLogo(
            Long organizationId) {

        Organization organization =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        organizationId
                                )
                        );


        if (organization.getLogoPath() == null ||
                organization.getLogoPath().isBlank()) {

            throw new ResourceNotFoundException(
                    "Organization logo",
                    organizationId
            );
        }


        try {

            Path logoPath =
                    Paths.get(
                                    organization.getLogoPath()
                            )
                            .toAbsolutePath()
                            .normalize();


            if (!Files.exists(logoPath) ||
                    !Files.isRegularFile(logoPath)) {

                throw new ResourceNotFoundException(
                        "Organization logo",
                        organizationId
                );
            }


            byte[] content =
                    Files.readAllBytes(
                            logoPath
                    );


            String contentType =
                    organization.getLogoContentType();

            if (contentType == null ||
                    contentType.isBlank()) {

                contentType =
                        Files.probeContentType(
                                logoPath
                        );
            }


            if (contentType == null) {
                contentType =
                        "application/octet-stream";
            }


            String fileName =
                    organization
                            .getLogoOriginalFileName();


            if (fileName == null ||
                    fileName.isBlank()) {

                fileName =
                        logoPath.getFileName()
                                .toString();
            }


            return new LogoFile(
                    content,
                    contentType,
                    fileName
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Unable to read organization logo.",
                    exception
            );
        }
    }


    // =========================================================
    // DELETE LOGO
    // =========================================================

    @Override
    public void deleteLogo(
            Long organizationId) {

        Organization organization =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        organizationId
                                )
                        );


        deleteExistingFile(
                organization
        );


        organization.setLogoPath(null);

        organization.setLogoContentType(null);

        organization.setLogoOriginalFileName(null);

        organizationRepository.save(
                organization
        );
    }


    // =========================================================
    // VALIDATE FILE
    // =========================================================

    private void validateFile(
            MultipartFile file) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Organization logo file is required."
            );
        }


        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Organization logo must not exceed 5 MB."
            );
        }


        String contentType =
                file.getContentType();


        if (contentType == null ||
                !isAllowedContentType(
                        contentType
                )) {

            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG and WEBP images are allowed."
            );
        }


        String fileName =
                file.getOriginalFilename();


        if (fileName == null ||
                fileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid logo file name."
            );
        }


        String extension =
                getExtension(fileName);


        if (!isAllowedExtension(
                extension
        )) {

            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG and WEBP images are allowed."
            );
        }
    }


    private boolean isAllowedContentType(
            String contentType) {

        return contentType.equalsIgnoreCase(
                "image/jpeg"
        )
                || contentType.equalsIgnoreCase(
                "image/png"
        )
                || contentType.equalsIgnoreCase(
                "image/webp"
        );
    }


    private boolean isAllowedExtension(
            String extension) {

        return extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")
                || extension.equalsIgnoreCase(".png")
                || extension.equalsIgnoreCase(".webp");
    }


    private String getExtension(
            String fileName) {

        if (fileName == null) {
            return "";
        }

        int index =
                fileName.lastIndexOf('.');

        if (index < 0) {
            return "";
        }

        return fileName.substring(index)
                .toLowerCase();
    }


    // =========================================================
    // DELETE EXISTING FILE
    // =========================================================

    private void deleteExistingFile(
            Organization organization) {

        String logoPath =
                organization.getLogoPath();

        if (logoPath == null ||
                logoPath.isBlank()) {

            return;
        }


        try {

            Path existingPath =
                    Paths.get(
                                    logoPath
                            )
                            .toAbsolutePath()
                            .normalize();


            if (Files.exists(existingPath)) {

                Files.deleteIfExists(
                        existingPath
                );
            }

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Unable to remove existing organization logo.",
                    exception
            );
        }
    }
}