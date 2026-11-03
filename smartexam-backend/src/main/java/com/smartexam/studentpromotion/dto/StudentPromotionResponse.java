package com.smartexam.studentpromotion.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentPromotionResponse {

    private int totalStudents;

    private int promotedStudents;

    private int failedStudents;

    private List<PromotionResult> results;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionResult {

        private Long studentId;

        private String studentName;

        private Long oldEnrollmentId;

        private Long newEnrollmentId;

        private boolean promoted;

        private String message;
    }
}