package com.smartexam.academic.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSessionSearchRequest {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "name";

    @Builder.Default
    private String direction = "ASC";

    private String keyword;

    private Long organizationId;

    private Boolean currentSession;

    private Boolean active;

}