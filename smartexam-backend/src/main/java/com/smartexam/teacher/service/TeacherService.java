package com.smartexam.teacher.service;

import com.smartexam.teacher.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {

    TeacherResponse create(
            TeacherRequest request);

    TeacherResponse update(
            Long id,
            TeacherUpdateRequest request);

    TeacherResponse getById(
            Long id);

    List<TeacherResponse> getAll();

    Page<TeacherResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<TeacherResponse> search(
            TeacherSearchRequest request);

    TeacherResponse activate(
            Long id);

    TeacherResponse deactivate(
            Long id);

    void delete(
            Long id);

    TeacherPhotoResponse uploadPhoto(
            Long teacherId,
            MultipartFile photo);

    void deletePhoto(
            Long teacherId);

}