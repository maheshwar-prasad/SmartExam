package com.smartexam.reportcard.controller;

import com.smartexam.reportcard.dto.StudentReportCardResponse;
import com.smartexam.reportcard.service.ReportCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report-cards")
public class ReportCardController {

    private final ReportCardService reportCardService;

    public ReportCardController(
            ReportCardService reportCardService) {

        this.reportCardService = reportCardService;
    }

    /**
     * Get complete report card for a student and exam.
     */
    @GetMapping("/student/{studentId}/exam/{examId}")
    public ResponseEntity<StudentReportCardResponse>
    getReportCard(
            @PathVariable Long studentId,
            @PathVariable Long examId) {

        return ResponseEntity.ok(
                reportCardService.getReportCard(
                        studentId,
                        examId
                )
        );
    }
}