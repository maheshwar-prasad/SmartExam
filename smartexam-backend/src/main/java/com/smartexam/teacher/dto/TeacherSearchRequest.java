package com.smartexam.teacher.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSearchRequest {

    @Builder.Default
    private String keyword = "";

    private Long organizationId;

    private Boolean active;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortBy = "employeeCode";

    @Builder.Default
    private String direction = "ASC";

}