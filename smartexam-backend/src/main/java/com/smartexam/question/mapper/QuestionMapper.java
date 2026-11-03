package com.smartexam.question.mapper;

import com.smartexam.question.dto.QuestionRequest;
import com.smartexam.question.dto.QuestionResponse;
import com.smartexam.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "active", ignore = true)
    Question toEntity(QuestionRequest request);

    @Mapping(
            target = "organizationId",
            source = "organization.id"
    )
    @Mapping(
            target = "subjectId",
            source = "subject.id"
    )
    QuestionResponse toResponse(Question question);
}