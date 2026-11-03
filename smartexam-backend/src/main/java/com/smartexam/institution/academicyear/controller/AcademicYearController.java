package com.smartexam.institution.academicyear.controller;

import com.smartexam.institution.academicyear.dto.AcademicYearRequest;
import com.smartexam.institution.academicyear.dto.AcademicYearResponse;
import com.smartexam.institution.academicyear.service.AcademicYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/academic-years")
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicYearResponse create(
            @Valid @RequestBody AcademicYearRequest request
    ) {
        return academicYearService.create(request);
    }

    @GetMapping("/{id}")
    public AcademicYearResponse getById(
            @PathVariable Long id,
            @RequestParam Long organizationId
    ) {
        return academicYearService.getById(
                id,
                organizationId
        );
    }

    @GetMapping
    public Page<@NotNull AcademicYearResponse> getAll(
            @RequestParam Long organizationId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "startDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction
    ) {

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return academicYearService.getAll(
                organizationId,
                pageable
        );
    }

    @PutMapping("/{id}")
    public AcademicYearResponse update(
            @PathVariable Long id,

            @RequestParam Long organizationId,

            @Valid @RequestBody AcademicYearRequest request
    ) {
        return academicYearService.update(
                id,
                organizationId,
                request
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestParam Long organizationId
    ) {
        academicYearService.delete(
                id,
                organizationId
        );
    }
}