package com.smartexam.question.service;

import com.smartexam.question.dto.QuestionRequest;
import com.smartexam.question.dto.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    QuestionResponse create(QuestionRequest request);

    QuestionResponse getById(Long id);

    List<QuestionResponse> getAll();

    Page<QuestionResponse> getAll(Pageable pageable);

    List<QuestionResponse> getBySubject(Long subjectId);

    QuestionResponse update(
            Long id,
            QuestionRequest request
    );

    void deactivate(Long id);
}