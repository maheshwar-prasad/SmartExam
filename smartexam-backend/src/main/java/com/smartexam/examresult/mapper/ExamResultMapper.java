package com.smartexam.examresult.mapper;

import com.smartexam.examresult.dto.ExamResultRequest;
import com.smartexam.examresult.dto.ExamResultResponse;
import com.smartexam.examresult.entity.ExamResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface ExamResultMapper {

    /**
     * Convert request to entity.
     *
     * Result calculation fields are ignored because
     * they will be calculated in the service.
     */
    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "totalSubjects",
            ignore = true
    )
    @Mapping(
            target = "totalMaximumMarks",
            ignore = true
    )
    @Mapping(
            target = "totalObtainedMarks",
            ignore = true
    )
    @Mapping(
            target = "percentage",
            ignore = true
    )
    @Mapping(
            target = "grade",
            ignore = true
    )
    @Mapping(
            target = "resultStatus",
            ignore = true
    )
    @Mapping(
            target = "published",
            ignore = true
    )
    @Mapping(
            target = "active",
            ignore = true
    )
    @Mapping(
            target = "createdAt",
            ignore = true
    )
    @Mapping(
            target = "updatedAt",
            ignore = true
    )
    ExamResult toEntity(
            ExamResultRequest request
    );

    /**
     * Convert entity to response.
     */
    ExamResultResponse toResponse(
            ExamResult examResult
    );

    /**
     * Convert entity list to response list.
     */
    List<ExamResultResponse> toResponseList(
            List<ExamResult> examResults
    );

    /**
     * Update editable fields only.
     *
     * Calculated fields must not be overwritten
     * directly from the request.
     */
    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "totalSubjects",
            ignore = true
    )
    @Mapping(
            target = "totalMaximumMarks",
            ignore = true
    )
    @Mapping(
            target = "totalObtainedMarks",
            ignore = true
    )
    @Mapping(
            target = "percentage",
            ignore = true
    )
    @Mapping(
            target = "grade",
            ignore = true
    )
    @Mapping(
            target = "resultStatus",
            ignore = true
    )
    @Mapping(
            target = "published",
            ignore = true
    )
    @Mapping(
            target = "active",
            ignore = true
    )
    @Mapping(
            target = "createdAt",
            ignore = true
    )
    @Mapping(
            target = "updatedAt",
            ignore = true
    )
    void updateEntity(
            ExamResultRequest request,
            @MappingTarget ExamResult examResult
    );
}