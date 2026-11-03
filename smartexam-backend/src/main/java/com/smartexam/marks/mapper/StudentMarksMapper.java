package com.smartexam.marks.mapper;

import com.smartexam.marks.dto.StudentMarksRequest;
import com.smartexam.marks.dto.StudentMarksResponse;
import com.smartexam.marks.entity.StudentMarks;
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
public interface StudentMarksMapper {

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "maximumMarks",
            ignore = true
    )
    @Mapping(
            target = "grade",
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
    StudentMarks toEntity(
            StudentMarksRequest request
    );

    StudentMarksResponse toResponse(
            StudentMarks studentMarks
    );

    List<StudentMarksResponse> toResponseList(
            List<StudentMarks> studentMarksList
    );

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "maximumMarks",
            ignore = true
    )
    @Mapping(
            target = "grade",
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
            StudentMarksRequest request,
            @MappingTarget StudentMarks studentMarks
    );
}