package com.smartexam.exam.mapper;

import com.smartexam.exam.dto.ExamRequest;
import com.smartexam.exam.dto.ExamResponse;
import com.smartexam.exam.entity.Exam;
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
public interface ExamMapper {

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
    Exam toEntity(ExamRequest request);


    ExamResponse toResponse(Exam exam);


    List<ExamResponse> toResponseList(
            List<Exam> exams
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
            ExamRequest request,
            @MappingTarget Exam exam
    );
}