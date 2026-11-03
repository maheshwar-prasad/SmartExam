package com.smartexam.reportcard.service;

import com.smartexam.reportcard.dto.StudentReportCardResponse;

public interface ReportCardService {

    /**
     * Get complete report card for a student and exam.
     */
    StudentReportCardResponse getReportCard(
            Long studentId,
            Long examId
    );
}