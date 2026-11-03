package com.smartexam.studentprofile.controller;

import com.smartexam.studentprofile.dto.StudentProfileResponse;
import com.smartexam.studentprofile.service.StudentProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-profiles")
@RequiredArgsConstructor
@Tag(
        name = "Student Profile",
        description = "Student Profile APIs"
)
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    /**
     * Get complete student profile.
     *
     * Includes:
     * - Student personal information
     * - Current enrollment
     * - Enrollment history
     * - Transfer history
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<@NotNull StudentProfileResponse> getStudentProfile(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentProfileService.getStudentProfile(studentId)
        );
    }
}