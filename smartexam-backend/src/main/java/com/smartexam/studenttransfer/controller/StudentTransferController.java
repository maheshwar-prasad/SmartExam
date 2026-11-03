package com.smartexam.studenttransfer.controller;

import com.smartexam.studenttransfer.dto.EligibleTransferStudentResponse;
import com.smartexam.studenttransfer.dto.StudentTransferRequest;
import com.smartexam.studenttransfer.dto.StudentTransferResponse;
import com.smartexam.studenttransfer.service.StudentTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-transfers")
@RequiredArgsConstructor
@Tag(
        name = "Student Transfer",
        description = "Student Transfer APIs"
)
public class StudentTransferController {

    private final StudentTransferService
            studentTransferService;


    @GetMapping("/eligible")
    @Operation(summary = "Get Student Transfer")
    public ResponseEntity<@NotNull List<EligibleTransferStudentResponse>>
    getEligibleStudents(

            @RequestParam Long academicSessionId,

            @RequestParam Long classRoomId,

            @RequestParam(required = false) Long sectionId) {

        return ResponseEntity.ok(
                studentTransferService.getEligibleStudents(
                        academicSessionId,
                        classRoomId,
                        sectionId
                )
        );
    }


    @PostMapping
    @Operation(summary = "Create Student Transfer")
    public ResponseEntity<@NotNull StudentTransferResponse>
    transferStudent(
            @Valid @RequestBody StudentTransferRequest request) {

        return ResponseEntity.ok(
                studentTransferService.transferStudent(
                        request
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get By Id")
    public ResponseEntity<@NotNull StudentTransferResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentTransferService.getById(id)
        );
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get By StudentId")
    public ResponseEntity<@NotNull List<StudentTransferResponse>> getByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentTransferService.getByStudentId(studentId)
        );
    }
}