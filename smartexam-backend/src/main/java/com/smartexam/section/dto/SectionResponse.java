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
public class SectionResponse {

    private Long id;

    private String name;

    private String code;

    private Integer displayOrder;

    private Integer capacity;

    private String description;

    private Boolean active;

    private Long organizationId;

    private String organizationName;

    private Long academicSessionId;

    private String academicSessionName;

    private Long classRoomId;

    private String classRoomName;

}