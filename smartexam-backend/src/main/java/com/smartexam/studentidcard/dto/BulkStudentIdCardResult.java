package com.smartexam.studentidcard.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkStudentIdCardResult {

    private Long studentId;

    private String studentName;

    private String status;

    private String message;

    private Long cardId;

    private String cardNumber;
}