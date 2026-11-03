package com.smartexam.studentwithdrawal.controller;

import com.smartexam.studentwithdrawal.dto.StudentWithdrawalRequest;
import com.smartexam.studentwithdrawal.dto.StudentWithdrawalResponse;
import com.smartexam.studentwithdrawal.service.StudentWithdrawalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-withdrawals")
@RequiredArgsConstructor
@Tag(
        name = "Student Withdrawal",
        description = "Student withdrawal management APIs"
)
public class StudentWithdrawalController {

    private final StudentWithdrawalService
            studentWithdrawalService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @Operation(summary = "Withdrawn Student")
    public ResponseEntity<StudentWithdrawalResponse> create(
            @Valid
            @RequestBody
            StudentWithdrawalRequest request
    ) {

        return ResponseEntity.ok(
                studentWithdrawalService.create(request)
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    @Operation(summary = "Update withdrawn Student by Id")
    public ResponseEntity<StudentWithdrawalResponse> update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            StudentWithdrawalRequest request
    ) {

        return ResponseEntity.ok(
                studentWithdrawalService.update(
                        id,
                        request
                )
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    @Operation(summary = "Get list of withdrawn Student by Id")
    public ResponseEntity<StudentWithdrawalResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                studentWithdrawalService.getById(id)
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    @Operation(summary = "Get All list withdrawn Student")
    public ResponseEntity<@NotNull Page<@NotNull StudentWithdrawalResponse>> getAll(

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "20"
            )
            int size
    ) {

        return ResponseEntity.ok(
                studentWithdrawalService.getAll(
                        PageRequest.of(page, size)
                )
        );
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate withdrawn Student by withdrawn Id")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        studentWithdrawalService.activate(id);

        return ResponseEntity.noContent().build();
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate withdrawn Student by withdrawn Id")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {
        studentWithdrawalService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}