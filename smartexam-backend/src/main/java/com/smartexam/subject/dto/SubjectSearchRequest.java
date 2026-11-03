package com.smartexam.subject.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectSearchRequest {

    @Builder.Default
    private String keyword = "";

    private Long organizationId;

    private Long academicSessionId;

    private Boolean active;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortBy = "displayOrder";

    @Builder.Default
    private String direction = "ASC";

}