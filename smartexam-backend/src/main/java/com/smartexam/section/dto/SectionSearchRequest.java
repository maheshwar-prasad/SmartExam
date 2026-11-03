package com.smartexam.section.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionSearchRequest {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "displayOrder";

    @Builder.Default
    private String direction = "ASC";

    private String keyword;

    private Long organizationId;

    private Long academicSessionId;

    private Long classRoomId;

    private Boolean active;

}