package com.smartexam.examschedule.mapper;

import com.smartexam.examschedule.dto.ExamScheduleRequest;
import com.smartexam.examschedule.dto.ExamScheduleResponse;
import com.smartexam.examschedule.entity.ExamSchedule;
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
public interface ExamScheduleMapper {

    @Mapping(
            target = "id",
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
    ExamSchedule toEntity(
            ExamScheduleRequest request
    );

    ExamScheduleResponse toResponse(
            ExamSchedule examSchedule
    );

    List<ExamScheduleResponse> toResponseList(
            List<ExamSchedule> examSchedules
    );

    @Mapping(
            target = "id",
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
            ExamScheduleRequest request,
            @MappingTarget ExamSchedule examSchedule
    );
}