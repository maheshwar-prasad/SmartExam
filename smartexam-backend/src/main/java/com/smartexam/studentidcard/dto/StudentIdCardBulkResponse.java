package com.smartexam.studentidcard.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentIdCardBulkResponse {

    private int total;

    private int success;

    private int failed;

    private List<BulkStudentIdCardResult> results;
}