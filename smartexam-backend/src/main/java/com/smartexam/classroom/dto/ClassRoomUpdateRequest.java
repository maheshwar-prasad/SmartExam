package com.smartexam.classroom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomUpdateRequest {

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

}