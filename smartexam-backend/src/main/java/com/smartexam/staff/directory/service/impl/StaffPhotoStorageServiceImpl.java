package com.smartexam.staff.directory.service.impl;

import com.smartexam.staff.directory.service.StaffPhotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class StaffPhotoStorageServiceImpl
        implements StaffPhotoStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/jpg",
                    "image/webp"
            );

    private static final long MAX_FILE_SIZE =
            2 * 1024 * 1024;

    private final Path uploadDirectory;

    public StaffPhotoStorageServiceImpl(
            @Value("${smartexam.file-upload.staff-photo-dir:uploads/staff}")
            String uploadDirectory
    ) {
        this.uploadDirectory =
                Paths.get(uploadDirectory)
                        .toAbsolutePath()
                        .normalize();
    }

    @Override
    public String store(
            MultipartFile photo,
            Long organizationId
    ) throws IOException {

        if (photo == null ||
                photo.isEmpty()) {
            return null;
        }

        if (photo.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Staff photo must not exceed 2 MB."
            );
        }

        String contentType =
                photo.getContentType();

        if (contentType == null ||
                !ALLOWED_CONTENT_TYPES.contains(contentType)) {

            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG and WEBP images are allowed."
            );
        }

        String originalFilename =
                photo.getOriginalFilename();

        String extension =
                getExtension(originalFilename);

        String filename =
                UUID.randomUUID()
                        + extension;

        Path organizationDirectory =
                uploadDirectory
                        .resolve(String.valueOf(organizationId))
                        .normalize();

        Files.createDirectories(
                organizationDirectory
        );

        Path destination =
                organizationDirectory
                        .resolve(filename)
                        .normalize();

        /*
         * Prevent path traversal.
         */
        if (!destination.startsWith(
                organizationDirectory
        )) {
            throw new IOException(
                    "Invalid file path."
            );
        }

        try (InputStream inputStream =
                     photo.getInputStream()) {

            Files.copy(
                    inputStream,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        /*
         * This URL will be returned by StaffResponse.
         *
         * We will expose /uploads/** from Spring Boot.
         */
        return "/uploads/staff/"
                + organizationId
                + "/"
                + filename;
    }

    @Override
    public void delete(
            String photoUrl
    ) {

        if (photoUrl == null ||
                photoUrl.isBlank()) {
            return;
        }

        try {

            String prefix =
                    "/uploads/staff/";

            if (!photoUrl.startsWith(prefix)) {
                return;
            }

            String relativePath =
                    photoUrl.substring(
                            prefix.length()
                    );

            Path file =
                    uploadDirectory
                            .resolve(relativePath)
                            .normalize();

            if (!file.startsWith(
                    uploadDirectory
            )) {
                return;
            }

            Files.deleteIfExists(file);

        } catch (IOException ignored) {
            /*
             * Do not fail staff deletion if
             * physical photo deletion fails.
             */
        }
    }

    private String getExtension(
            String filename
    ) {

        if (filename == null ||
                filename.isBlank()) {
            return ".jpg";
        }

        int dotIndex =
                filename.lastIndexOf('.');

        if (dotIndex < 0) {
            return ".jpg";
        }

        return filename
                .substring(dotIndex)
                .toLowerCase();
    }
}