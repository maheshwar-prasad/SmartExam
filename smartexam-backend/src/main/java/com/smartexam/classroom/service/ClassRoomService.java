package com.smartexam.classroom.service;

import com.smartexam.classroom.dto.ClassRoomRequest;
import com.smartexam.classroom.dto.ClassRoomResponse;
import com.smartexam.classroom.dto.ClassRoomSearchRequest;
import com.smartexam.classroom.dto.ClassRoomUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassRoomService {

    /**
     * Create Class Room
     */
    ClassRoomResponse create(ClassRoomRequest request);

    /**
     * Update Class Room
     */
    ClassRoomResponse update(
            Long id,
            ClassRoomUpdateRequest request);

    /**
     * Get by Id
     */
    ClassRoomResponse getById(Long id);

    /**
     * Get All
     */
    List<ClassRoomResponse> getAll();

    /**
     * Get All with Pagination
     */
    Page<ClassRoomResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    /**
     * Search
     */
    Page<ClassRoomResponse> search(
            ClassRoomSearchRequest request);

    /**
     * Activate
     */
    ClassRoomResponse activate(Long id);

    /**
     * Deactivate
     */
    ClassRoomResponse deactivate(Long id);

    /**
     * Soft Delete
     */
    void delete(Long id);

}