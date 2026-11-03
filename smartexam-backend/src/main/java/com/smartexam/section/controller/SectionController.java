package com.smartexam.section.controller;

import com.smartexam.section.dto.SectionRequest;
import com.smartexam.section.dto.SectionResponse;
import com.smartexam.section.dto.SectionSearchRequest;
import com.smartexam.section.dto.SectionUpdateRequest;
import com.smartexam.section.service.SectionService;
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
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
@Tag(name = "Section", description = "Section Management APIs")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    @Operation(summary = "Create Section")
    public ResponseEntity<SectionResponse> create(

            @Valid
            @RequestBody
            SectionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sectionService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Section")
    public ResponseEntity<SectionResponse> update(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            SectionUpdateRequest request) {

        return ResponseEntity.ok(
                sectionService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Section By Id")
    public ResponseEntity<SectionResponse> getById(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                sectionService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Sections")
    public ResponseEntity<List<SectionResponse>> getAll() {

        return ResponseEntity.ok(
                sectionService.getAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get All Sections With Pagination")
    public ResponseEntity<Page<SectionResponse>> getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "displayOrder")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return ResponseEntity.ok(
                sectionService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    @PostMapping("/search")
    @Operation(summary = "Search Sections")
    public ResponseEntity<Page<SectionResponse>> search(

            @RequestBody
            SectionSearchRequest request) {

        return ResponseEntity.ok(
                sectionService.search(request));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Section")
    public ResponseEntity<SectionResponse> activate(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                sectionService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Section")
    public ResponseEntity<SectionResponse> deactivate(

            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                sectionService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Section")
    public ResponseEntity<Void> delete(
            @PathVariable
            Long id) {
        sectionService.delete(id);

        return ResponseEntity.noContent().build();
    }

}