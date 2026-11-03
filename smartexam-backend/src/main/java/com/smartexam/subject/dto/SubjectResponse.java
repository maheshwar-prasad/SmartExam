package com.smartexam.subject.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Integer theoryMarks;

    private Integer practicalMarks;

    private Integer passMarks;

    private Integer displayOrder;

    private Boolean active;

    private Long organizationId;

    private String organizationName;

    private Long academicSessionId;

    private String academicSessionName;

}