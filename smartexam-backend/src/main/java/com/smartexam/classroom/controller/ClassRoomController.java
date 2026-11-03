package com.smartexam.classroom.controller;

import com.smartexam.classroom.dto.ClassRoomRequest;
import com.smartexam.classroom.dto.ClassRoomResponse;
import com.smartexam.classroom.dto.ClassRoomSearchRequest;
import com.smartexam.classroom.dto.ClassRoomUpdateRequest;
import com.smartexam.classroom.service.ClassRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classrooms")
@RequiredArgsConstructor
@Tag(name = "ClassRoom", description = "ClassRoom Management APIs")
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @PostMapping
    @Operation(summary = "Create ClassRoom")
    public ResponseEntity<ClassRoomResponse> create(

            @Valid
            @RequestBody
            ClassRoomRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(classRoomService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ClassRoom")
    public ResponseEntity<ClassRoomResponse> update(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            ClassRoomUpdateRequest request) {

        return ResponseEntity.ok(
                classRoomService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ClassRoom By Id")
    public ResponseEntity<ClassRoomResponse> getById(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                classRoomService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All ClassRooms")
    public ResponseEntity<List<ClassRoomResponse>> getAll() {

        return ResponseEntity.ok(
                classRoomService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get All ClassRooms With Pagination")
    public ResponseEntity<Page<ClassRoomResponse>> getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "displayOrder")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return ResponseEntity.ok(
                classRoomService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search ClassRooms")
    public ResponseEntity<Page<ClassRoomResponse>> search(

            @RequestBody
            ClassRoomSearchRequest request) {

        return ResponseEntity.ok(
                classRoomService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate ClassRoom")
    public ResponseEntity<ClassRoomResponse> activate(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                classRoomService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate ClassRoom")
    public ResponseEntity<ClassRoomResponse> deactivate(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                classRoomService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ClassRoom")
    public ResponseEntity<Void> delete(

            @PathVariable
            Long id) {

        classRoomService.delete(id);

        return ResponseEntity.noContent().build();
    }

}