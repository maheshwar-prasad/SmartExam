package com.smartexam.question.service.impl;

import com.smartexam.question.dto.QuestionRequest;
import com.smartexam.question.dto.QuestionResponse;
import com.smartexam.question.entity.Question;
import com.smartexam.question.mapper.QuestionMapper;
import com.smartexam.question.repository.QuestionRepository;
import com.smartexam.question.service.QuestionService;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.subject.entity.Subject;
import com.smartexam.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final SubjectRepository subjectRepository;
    private final CurrentUserService currentUserService;

    @Override
    public QuestionResponse create(
            QuestionRequest request) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Subject subject =
                getSubject(
                        request.subjectId(),
                        organizationId
                );

        Question question =
                questionMapper.toEntity(request);

        question.setOrganization(
                currentUserService
                        .getCurrentUser()
                        .getUser()
                        .getOrganization()
        );

        question.setSubject(subject);

        question.setActive(true);

        Question saved =
                questionRepository.save(question);

        return questionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getById(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Question question =
                questionRepository
                        .findByIdAndOrganizationIdAndActiveTrue(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Question not found"
                                )
                        );

        return questionMapper.toResponse(question);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getAll() {

        Long organizationId =
                currentUserService.getOrganizationId();

        return questionRepository
                .findAllByOrganizationIdAndActiveTrue(
                        organizationId
                )
                .stream()
                .map(questionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> getAll(
            Pageable pageable) {

        Long organizationId =
                currentUserService.getOrganizationId();

        return questionRepository
                .findAllByOrganizationIdAndActiveTrue(
                        organizationId,
                        pageable
                )
                .map(questionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getBySubject(
            Long subjectId) {

        Long organizationId =
                currentUserService.getOrganizationId();

        // Verify subject belongs to current organization
        getSubject(subjectId, organizationId);

        return questionRepository
                .findAllBySubjectIdAndOrganizationIdAndActiveTrue(
                        subjectId,
                        organizationId
                )
                .stream()
                .map(questionMapper::toResponse)
                .toList();
    }

    @Override
    public QuestionResponse update(
            Long id,
            QuestionRequest request) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Question question =
                questionRepository
                        .findByIdAndOrganizationIdAndActiveTrue(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Question not found"
                                )
                        );

        Subject subject =
                getSubject(
                        request.subjectId(),
                        organizationId
                );

        question.setQuestionText(
                request.questionText()
        );

        question.setQuestionType(
                request.questionType()
        );

        question.setDifficulty(
                request.difficulty()
        );

        question.setOptionA(
                request.optionA()
        );

        question.setOptionB(
                request.optionB()
        );

        question.setOptionC(
                request.optionC()
        );

        question.setOptionD(
                request.optionD()
        );

        question.setCorrectAnswer(
                request.correctAnswer()
        );

        question.setExplanation(
                request.explanation()
        );

        question.setMarks(
                request.marks()
        );

        question.setSubject(subject);

        return questionMapper.toResponse(
                questionRepository.save(question)
        );
    }

    @Override
    public void deactivate(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Question question =
                questionRepository
                        .findByIdAndOrganizationIdAndActiveTrue(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Question not found"
                                )
                        );

        question.setActive(false);

        questionRepository.save(question);
    }

    private Subject getSubject(
            Long subjectId,
            Long organizationId) {

        return subjectRepository
                .findByIdAndOrganizationIdAndActiveTrue(
                        subjectId,
                        organizationId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Subject not found for this organization"
                        )
                );
    }
}