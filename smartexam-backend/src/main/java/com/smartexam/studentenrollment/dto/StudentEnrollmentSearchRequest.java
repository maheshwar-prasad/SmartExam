package com.smartexam.studentenrollment.dto;

import com.smartexam.common.enums.EnrollmentStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentSearchRequest {

    @Builder.Default
    private String keyword = "";

    private Long studentId;

    private Long academicSessionId;

    private Long classRoomId;

    private Long sectionId;

    private EnrollmentStatus enrollmentStatus;

    private Boolean active;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortBy = "id";

    @Builder.Default
    private String direction = "ASC";

}