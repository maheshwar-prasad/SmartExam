package com.smartexam.timetable.controller;

import com.smartexam.timetable.dto.TimetableRequest;
import com.smartexam.timetable.dto.TimetableResponse;
import com.smartexam.timetable.dto.TimetableSearchRequest;
import com.smartexam.timetable.dto.TimetableUpdateRequest;
import com.smartexam.timetable.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetables")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Timetable",
        description = "Timetable Management APIs"
)
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping
    @Operation(summary = "Create Timetable")
    public ResponseEntity<TimetableResponse> create(
            @Valid @RequestBody TimetableRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(timetableService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Timetable")
    public ResponseEntity<TimetableResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TimetableUpdateRequest request) {

        return ResponseEntity.ok(
                timetableService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Timetable By Id")
    public ResponseEntity<TimetableResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                timetableService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Active Timetables")
    public ResponseEntity<List<TimetableResponse>> getAll() {

        return ResponseEntity.ok(
                timetableService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Timetables With Pagination")
    public ResponseEntity<Page<TimetableResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dayOfWeek") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(
                timetableService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Timetables")
    public ResponseEntity<Page<TimetableResponse>> search(
            @RequestBody TimetableSearchRequest request) {

        return ResponseEntity.ok(
                timetableService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Timetable")
    public ResponseEntity<TimetableResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                timetableService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Timetable")
    public ResponseEntity<TimetableResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                timetableService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Timetable")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        timetableService.delete(id);

        return ResponseEntity.noContent().build();
    }
}