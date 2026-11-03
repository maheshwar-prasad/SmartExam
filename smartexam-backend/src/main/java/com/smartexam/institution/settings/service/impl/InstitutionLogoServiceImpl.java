package com.smartexam.institution.settings.service.impl;

import com.smartexam.institution.settings.dto.FileUploadResponse;
import com.smartexam.institution.settings.service.InstitutionLogoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class InstitutionLogoServiceImpl implements InstitutionLogoService {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg",
            "jpeg",
            "png",
            "webp"
    );

    @Value("${smartexam.storage.institution-logos.location:./uploads/institution-logos}")
    private String storageLocation;

    @Override
    public FileUploadResponse uploadLogo(
            Long organizationId,
            MultipartFile file
    ) {

        validateFile(file);

        try {
            Path organizationDirectory = Paths.get(storageLocation)
                    .toAbsolutePath()
                    .normalize()
                    .resolve(String.valueOf(organizationId));

            Files.createDirectories(organizationDirectory);

            String originalFileName = StringUtils.cleanPath(
                    file.getOriginalFilename() == null
                            ? "logo"
                            : file.getOriginalFilename()
            );

            String extension = getFileExtension(originalFileName);

            String generatedFileName = UUID.randomUUID()
                    + "."
                    + extension;

            Path targetFile = organizationDirectory.resolve(generatedFileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        targetFile,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            String fileUrl = "uploads/organization-logos/"
                    + organizationId
                    + "/"
                    + generatedFileName;

            return new FileUploadResponse(
                    fileUrl,
                    generatedFileName,
                    originalFileName,
                    file.getContentType(),
                    file.getSize()
            );

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to upload institution logo.",
                    exception
            );
        }
    }

    @Override
    public void deleteLogo(String logoUrl) {

        if (!StringUtils.hasText(logoUrl)) {
            return;
        }

        try {
            String relativePath = logoUrl;

            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }

            if (relativePath.startsWith("uploads/organization-logos/")) {
                relativePath = relativePath.substring("uploads/".length());
            }

            Path filePath = Paths.get(storageLocation)
                    .toAbsolutePath()
                    .normalize()
                    .resolve(
                            relativePath.replace(
                                    "organization-logos/",
                                    ""
                            )
                    )
                    .normalize();

            Path storageRoot = Paths.get(storageLocation)
                    .toAbsolutePath()
                    .normalize();

            if (!filePath.startsWith(storageRoot)) {
                throw new IllegalArgumentException(
                        "Invalid logo file path."
                );
            }

            Files.deleteIfExists(filePath);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to delete institution logo.",
                    exception
            );
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Logo file is required."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Logo file size must not exceed 2 MB."
            );
        }

        String contentType = file.getContentType();

        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are allowed."
            );
        }

        String originalFileName = file.getOriginalFilename();

        if (!StringUtils.hasText(originalFileName)) {
            throw new IllegalArgumentException(
                    "Invalid logo file name."
            );
        }

        String extension = getFileExtension(originalFileName);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP files are allowed."
            );
        }
    }

    private String getFileExtension(String fileName) {

        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException(
                    "Logo file must have a valid extension."
            );
        }

        return fileName
                .substring(lastDotIndex + 1)
                .toLowerCase();
    }
}