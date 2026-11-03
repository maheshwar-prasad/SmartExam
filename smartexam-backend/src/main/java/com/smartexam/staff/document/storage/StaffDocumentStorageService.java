package com.smartexam.staff.document.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class StaffDocumentStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf",
            "jpg",
            "jpeg",
            "png"
    );

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    private final Path storageLocation;

    public StaffDocumentStorageService(
            @Value("${smartexam.storage.staff-documents.location:./uploads/staff-documents}")
            String storageLocation
    ) {

        try {

            this.storageLocation =
                    Paths.get(storageLocation)
                            .toAbsolutePath()
                            .normalize();

            Files.createDirectories(this.storageLocation);

        } catch (IOException ex) {

            throw new IllegalStateException(
                    "Could not initialize staff document storage location: "
                            + storageLocation,
                    ex
            );
        }
    }


    /**
     * Stores the uploaded file and returns storage information.
     */
    public StoredFile store(MultipartFile file) {

        validateFile(file);

        String originalFileName =
                StringUtils.cleanPath(
                        file.getOriginalFilename() == null
                                ? "document"
                                : file.getOriginalFilename()
                );

        String extension =
                getExtension(originalFileName);

        String storedFileName =
                UUID.randomUUID()
                        + "."
                        + extension;

        Path targetLocation =
                storageLocation
                        .resolve(storedFileName)
                        .normalize();

        if (!targetLocation.startsWith(storageLocation)) {

            throw new IllegalArgumentException(
                    "Invalid file path."
            );
        }

        try (InputStream inputStream =
                     file.getInputStream()) {

            Files.copy(
                    inputStream,
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException ex) {

            throw new IllegalStateException(
                    "Failed to store staff document file.",
                    ex
            );
        }

        return new StoredFile(
                originalFileName,
                storedFileName,
                targetLocation.toString(),
                file.getContentType(),
                file.getSize()
        );
    }


    /**
     * Loads a stored file as a Spring Resource.
     */
    public Resource loadAsResource(
            String storedFileName
    ) {

        if (!StringUtils.hasText(storedFileName)) {

            throw new IllegalArgumentException(
                    "Stored file name is required."
            );
        }

        try {

            Path filePath =
                    storageLocation
                            .resolve(storedFileName)
                            .normalize();

            if (!filePath.startsWith(storageLocation)) {

                throw new IllegalArgumentException(
                        "Invalid file path."
                );
            }

            if (!Files.exists(filePath)) {

                throw new IllegalArgumentException(
                        "Staff document file not found."
                );
            }

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (!resource.exists() ||
                    !resource.isReadable()) {

                throw new IllegalArgumentException(
                        "Staff document file is not readable."
                );
            }

            return resource;

        } catch (MalformedURLException ex) {

            throw new IllegalStateException(
                    "Could not load staff document file.",
                    ex
            );
        }
    }


    /**
     * Deletes a stored file.
     */
    public void delete(
            String storedFileName
    ) {

        if (!StringUtils.hasText(storedFileName)) {
            return;
        }

        Path filePath =
                storageLocation
                        .resolve(storedFileName)
                        .normalize();

        if (!filePath.startsWith(storageLocation)) {

            throw new IllegalArgumentException(
                    "Invalid file path."
            );
        }

        try {

            Files.deleteIfExists(filePath);

        } catch (IOException ex) {

            throw new IllegalStateException(
                    "Failed to delete staff document file.",
                    ex
            );
        }
    }


    /**
     * Checks whether a stored file exists.
     */
    public boolean exists(
            String storedFileName
    ) {

        if (!StringUtils.hasText(storedFileName)) {
            return false;
        }

        Path filePath =
                storageLocation
                        .resolve(storedFileName)
                        .normalize();

        if (!filePath.startsWith(storageLocation)) {
            return false;
        }

        return Files.exists(filePath);
    }


    private void validateFile(
            MultipartFile file
    ) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Staff document file is required."
            );
        }


        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Staff document file size must not exceed 5 MB."
            );
        }


        String originalFileName =
                StringUtils.cleanPath(
                        file.getOriginalFilename() == null
                                ? ""
                                : file.getOriginalFilename()
                );

        if (!StringUtils.hasText(
                originalFileName
        )) {

            throw new IllegalArgumentException(
                    "Staff document file name is required."
            );
        }


        String extension =
                getExtension(originalFileName);

        if (!ALLOWED_EXTENSIONS.contains(
                extension
        )) {

            throw new IllegalArgumentException(
                    "Unsupported staff document file type. "
                            + "Allowed types: PDF, JPG, JPEG, PNG."
            );
        }


        String contentType =
                file.getContentType();

        if (!StringUtils.hasText(
                contentType
        )) {

            throw new IllegalArgumentException(
                    "Unable to determine the file type."
            );
        }

        contentType =
                contentType
                        .toLowerCase(Locale.ROOT)
                        .trim();

        if (!ALLOWED_CONTENT_TYPES.contains(
                contentType
        )) {

            throw new IllegalArgumentException(
                    "Unsupported staff document content type."
            );
        }


        validateExtensionAndContentType(
                extension,
                contentType
        );
    }


    private void validateExtensionAndContentType(
            String extension,
            String contentType
    ) {

        boolean valid =
                switch (extension) {

                    case "pdf" ->
                            "application/pdf"
                                    .equals(contentType);

                    case "jpg", "jpeg" ->
                            "image/jpeg"
                                    .equals(contentType);

                    case "png" ->
                            "image/png"
                                    .equals(contentType);

                    default ->
                            false;
                };

        if (!valid) {

            throw new IllegalArgumentException(
                    "File extension does not match the file content type."
            );
        }
    }


    private String getExtension(
            String fileName
    ) {

        int lastDot =
                fileName.lastIndexOf('.');

        if (lastDot < 0 ||
                lastDot == fileName.length() - 1) {

            throw new IllegalArgumentException(
                    "File extension is required."
            );
        }

        return fileName
                .substring(lastDot + 1)
                .toLowerCase(Locale.ROOT);
    }


    /**
     * Information about the stored file.
     */
    public record StoredFile(
            String originalFileName,
            String storedFileName,
            String filePath,
            String contentType,
            long fileSize
    ) {
    }
}