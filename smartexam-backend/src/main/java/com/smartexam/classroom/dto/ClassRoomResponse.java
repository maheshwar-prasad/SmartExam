package com.smartexam.classroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomResponse {

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

}