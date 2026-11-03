package com.smartexam.teacher.service.impl;

import com.smartexam.teacher.service.FileStorageService;
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
public class FileStorageServiceImpl implements FileStorageService {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "webp");

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    private final Path rootDirectory;

    public FileStorageServiceImpl(
            @Value("${smartexam.file-upload.root-dir:uploads}")
            String rootDirectory) {

        this.rootDirectory =
                Paths.get(rootDirectory)
                        .toAbsolutePath()
                        .normalize();
    }

    @Override
    public String store(
            MultipartFile file,
            String folder,
            Long organizationId) {

        validate(file);

        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID is required");
        }

        String originalFilename =
                StringUtils.cleanPath(
                        file.getOriginalFilename() == null
                                ? ""
                                : file.getOriginalFilename()
                );

        String extension =
                getExtension(originalFilename);

        String filename =
                UUID.randomUUID() + "." + extension;

        Path directory =
                rootDirectory
                        .resolve(folder)
                        .resolve(String.valueOf(organizationId))
                        .normalize();

        try {

            Files.createDirectories(directory);

            Path target =
                    directory
                            .resolve(filename)
                            .normalize();

            if (!target.startsWith(directory)) {
                throw new IllegalArgumentException(
                        "Invalid file path");
            }

            try (InputStream inputStream =
                         file.getInputStream()) {

                Files.copy(
                        inputStream,
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            return "/uploads/"
                    + folder
                    + "/"
                    + organizationId
                    + "/"
                    + filename;

        } catch (IOException ex) {

            throw new IllegalStateException(
                    "Failed to store file",
                    ex
            );
        }
    }

    @Override
    public void delete(String fileUrl) {

        if (!StringUtils.hasText(fileUrl)) {
            return;
        }

        if (!fileUrl.startsWith("/uploads/")) {
            return;
        }

        String relativePath =
                fileUrl.substring("/uploads/".length());

        Path file =
                rootDirectory
                        .resolve(relativePath)
                        .normalize();

        if (!file.startsWith(rootDirectory)) {
            return;
        }

        try {

            Files.deleteIfExists(file);

        } catch (IOException ex) {

            // Do not fail the business operation
            // because old photo deletion failed.
            System.err.println(
                    "Failed to delete file: "
                            + fileUrl
            );
        }
    }

    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Photo is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Photo size must not exceed 2 MB");
        }

        String contentType =
                file.getContentType();

        if (!ALLOWED_CONTENT_TYPES.contains(
                contentType)) {

            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are allowed");
        }

        String extension =
                getExtension(
                        file.getOriginalFilename()
                );

        if (!ALLOWED_EXTENSIONS.contains(
                extension.toLowerCase())) {

            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG and WEBP images are allowed");
        }
    }

    private String getExtension(
            String filename) {

        if (!StringUtils.hasText(filename)) {
            throw new IllegalArgumentException(
                    "File name is required");
        }

        int index =
                filename.lastIndexOf('.');

        if (index < 0 ||
                index == filename.length() - 1) {

            throw new IllegalArgumentException(
                    "File extension is required");
        }

        return filename
                .substring(index + 1)
                .toLowerCase();
    }
}