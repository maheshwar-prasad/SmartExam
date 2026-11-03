package com.smartexam.staff.directory.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StaffPhotoStorageService {

    String store(
            MultipartFile photo,
            Long organizationId
    ) throws IOException;

    void delete(
            String photoUrl
    );
}