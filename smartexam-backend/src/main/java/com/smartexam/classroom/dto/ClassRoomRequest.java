package com.smartexam.classroom.dto;

import com.smartexam.common.entity.BaseEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomRequest extends BaseEntity {

    @NotBlank(message = "Class name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Class code is required")
    @Size(max = 20)
    private String code;

    @NotNull(message = "Display order is required")
    @Min(value = 1)
    private Integer displayOrder;

    @NotNull(message = "Capacity is required")
    @Min(value = 1)
    private Integer capacity;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Organization is required")
    private Long organizationId;

    @NotNull(message = "Academic Session is required")
    private Long academicSessionId;

}