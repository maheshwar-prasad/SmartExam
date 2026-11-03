package com.smartexam.section.dto;

import com.smartexam.common.entity.BaseEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SectionRequest extends BaseEntity {

    @NotBlank(message = "Section name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Section code is required")
    @Size(max = 20)
    private String code;

    @NotNull(message = "Display order is required")
    @Min(1)
    private Integer displayOrder;

    @NotNull(message = "Capacity is required")
    @Min(1)
    private Integer capacity;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Organization is required")
    private Long organizationId;

    @NotNull(message = "Academic Session is required")
    private Long academicSessionId;

    @NotNull(message = "ClassRoom is required")
    private Long classRoomId;

}