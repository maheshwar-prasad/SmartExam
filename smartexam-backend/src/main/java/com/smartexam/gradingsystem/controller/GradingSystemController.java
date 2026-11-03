package com.smartexam.gradingsystem.controller;

import com.smartexam.gradingsystem.dto.GradingSystemRequest;
import com.smartexam.gradingsystem.dto.GradingSystemResponse;
import com.smartexam.gradingsystem.service.GradingSystemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grading-systems")
@RequiredArgsConstructor
public class GradingSystemController {

    private final GradingSystemService gradingSystemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GradingSystemResponse create(
            @Valid @RequestBody GradingSystemRequest request
    ) {
        return gradingSystemService.create(request);
    }

    @PutMapping("/{id}")
    public GradingSystemResponse update(
            @PathVariable Long id,
            @Valid @RequestBody GradingSystemRequest request
    ) {
        return gradingSystemService.update(id, request);
    }

    @GetMapping
    public List<GradingSystemResponse> getAll() {
        return gradingSystemService.getAll();
    }

    @GetMapping("/{id}")
    public GradingSystemResponse getById(@PathVariable Long id) {
        return gradingSystemService.getById(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<GradingSystemResponse> getByOrganization(
            @PathVariable Long organizationId
    ) {
        return gradingSystemService.getByOrganization(organizationId);
    }

    @GetMapping("/organization/{organizationId}/active")
    public List<GradingSystemResponse> getActiveByOrganization(
            @PathVariable Long organizationId
    ) {
        return gradingSystemService.getActiveByOrganization(organizationId);
    }

    @PatchMapping("/{id}/activate")
    public GradingSystemResponse activate(@PathVariable Long id) {
        return gradingSystemService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public GradingSystemResponse deactivate(@PathVariable Long id) {
        return gradingSystemService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gradingSystemService.delete(id);
    }
}