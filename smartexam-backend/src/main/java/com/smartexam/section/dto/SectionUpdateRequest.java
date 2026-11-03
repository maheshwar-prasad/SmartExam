package com.smartexam.section.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
public class SectionUpdateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String code;

    @Min(1)
    private Integer displayOrder;

    @Min(1)
    private Integer capacity;

    @Size(max = 500)
    private String description;

    private Long academicSessionId;

    private Long classRoomId;

}