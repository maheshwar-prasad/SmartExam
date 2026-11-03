package com.smartexam.teacher.controller;

import com.smartexam.teacher.dto.*;
import com.smartexam.teacher.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Teacher", description = "Teacher Management APIs")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @Operation(summary = "Create Teacher")
    public ResponseEntity<TeacherResponse> create(
            @Valid @RequestBody TeacherRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teacherService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Teacher")
    public ResponseEntity<TeacherResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TeacherUpdateRequest request) {

        return ResponseEntity.ok(
                teacherService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Teacher By Id")
    public ResponseEntity<TeacherResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                teacherService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Teachers")
    public ResponseEntity<List<TeacherResponse>> getAll() {

        return ResponseEntity.ok(
                teacherService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get Teachers With Pagination")
    public ResponseEntity<Page<TeacherResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeCode") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(
                teacherService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Teachers")
    public ResponseEntity<Page<TeacherResponse>> search(
            @RequestBody TeacherSearchRequest request) {

        return ResponseEntity.ok(
                teacherService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Teacher")
    public ResponseEntity<TeacherResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                teacherService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Teacher")
    public ResponseEntity<TeacherResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                teacherService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Teacher")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        teacherService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<TeacherPhotoResponse> uploadPhoto(
            @PathVariable Long id,
            @RequestPart("photo") MultipartFile photo) {

        return ResponseEntity.ok(
                teacherService.uploadPhoto(id, photo)
        );
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable Long id) {

        teacherService.deletePhoto(id);

        return ResponseEntity.noContent().build();
    }
}