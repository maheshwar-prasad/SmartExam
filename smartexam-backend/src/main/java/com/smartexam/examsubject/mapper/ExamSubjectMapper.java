package com.smartexam.examsubject.mapper;

import com.smartexam.examsubject.dto.ExamSubjectRequest;
import com.smartexam.examsubject.dto.ExamSubjectResponse;
import com.smartexam.examsubject.entity.ExamSubject;
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
public interface ExamSubjectMapper {

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
    ExamSubject toEntity(
            ExamSubjectRequest request
    );

    ExamSubjectResponse toResponse(
            ExamSubject examSubject
    );

    List<ExamSubjectResponse> toResponseList(
            List<ExamSubject> examSubjects
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
            ExamSubjectRequest request,
            @MappingTarget ExamSubject examSubject
    );
}