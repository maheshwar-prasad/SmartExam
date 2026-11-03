package com.smartexam.studentpromotion.controller;

import com.smartexam.studentpromotion.dto.EligibleStudentResponse;
import com.smartexam.studentpromotion.dto.StudentPromotionRequest;
import com.smartexam.studentpromotion.dto.StudentPromotionResponse;
import com.smartexam.studentpromotion.service.StudentPromotionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-promotions")
@RequiredArgsConstructor
public class StudentPromotionController {

    private final StudentPromotionService studentPromotionService;


    /**
     * Get students eligible for promotion.
     */
    @GetMapping("/eligible")
    @Tag(name = "Promotion API", description = "Promotion Management APIs")
    public ResponseEntity<@NotNull List<EligibleStudentResponse>> getEligibleStudents(
            @RequestParam Long academicSessionId,
            @RequestParam Long classRoomId,
            @RequestParam(required = false) Long sectionId) {

        return ResponseEntity.ok(
                studentPromotionService.getEligibleStudents(
                        academicSessionId,
                        classRoomId,
                        sectionId
                )
        );
    }


    /**
     * Promote selected students.
     */
    @PostMapping
    public ResponseEntity<@NotNull StudentPromotionResponse> promoteStudents(
            @Valid @RequestBody StudentPromotionRequest request) {

        return ResponseEntity.ok(
                studentPromotionService.promoteStudents(request)
        );
    }
}