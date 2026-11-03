package com.smartexam.staff.document.controller;

import com.smartexam.staff.document.dto.StaffDocumentTypeRequest;
import com.smartexam.staff.document.dto.StaffDocumentTypeResponse;
import com.smartexam.staff.document.dto.StaffDocumentTypeUpdateRequest;
import com.smartexam.staff.document.service.StaffDocumentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff-document-types")
@RequiredArgsConstructor
public class StaffDocumentTypeController {

    private final StaffDocumentTypeService documentTypeService;

    @PostMapping
    public ResponseEntity<StaffDocumentTypeResponse> create(
            @Valid @RequestBody StaffDocumentTypeRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentTypeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDocumentTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffDocumentTypeUpdateRequest request
    ) {

        return ResponseEntity.ok(
                documentTypeService.update(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDocumentTypeResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentTypeService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<StaffDocumentTypeResponse>> getAll() {

        return ResponseEntity.ok(
                documentTypeService.getAll()
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<StaffDocumentTypeResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {

        return ResponseEntity.ok(
                documentTypeService.getAll(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<StaffDocumentTypeResponse>>
    getByOrganization(
            @PathVariable Long organizationId
    ) {

        return ResponseEntity.ok(
                documentTypeService.getByOrganization(
                        organizationId
                )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StaffDocumentTypeResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentTypeService.activate(id)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<StaffDocumentTypeResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentTypeService.deactivate(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        documentTypeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}