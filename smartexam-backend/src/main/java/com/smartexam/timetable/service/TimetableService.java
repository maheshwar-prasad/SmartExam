package com.smartexam.timetable.service;

import com.smartexam.timetable.dto.TimetableRequest;
import com.smartexam.timetable.dto.TimetableResponse;
import com.smartexam.timetable.dto.TimetableSearchRequest;
import com.smartexam.timetable.dto.TimetableUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TimetableService {

    /**
     * Create a new timetable entry.
     */
    TimetableResponse create(
            TimetableRequest request);

    /**
     * Update an existing timetable entry.
     */
    TimetableResponse update(
            Long id,
            TimetableUpdateRequest request);

    /**
     * Get timetable by ID.
     */
    TimetableResponse getById(
            Long id);

    /**
     * Get all timetable entries.
     */
    List<TimetableResponse> getAll();

    /**
     * Get timetable entries with pagination.
     */
    Page<TimetableResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    /**
     * Search timetable entries using dynamic filters.
     */
    Page<TimetableResponse> search(
            TimetableSearchRequest request);

    /**
     * Activate a timetable entry.
     */
    TimetableResponse activate(
            Long id);

    /**
     * Deactivate a timetable entry.
     */
    TimetableResponse deactivate(
            Long id);

    /**
     * Delete a timetable entry.
     */
    void delete(
            Long id);


}