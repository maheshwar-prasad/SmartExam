package com.smartexam.exam.service.impl;

import com.smartexam.common.enums.NotificationType;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.exam.dto.ExamRequest;
import com.smartexam.exam.dto.ExamResponse;
import com.smartexam.exam.entity.Exam;
import com.smartexam.exam.mapper.ExamMapper;
import com.smartexam.exam.repository.ExamRepository;
import com.smartexam.exam.service.ExamService;
import com.smartexam.examregistration.entity.StudentExamRegistration;
import com.smartexam.examregistration.repository.StudentExamRegistrationRepository;
import com.smartexam.notification.event.NotificationEvent;
import com.smartexam.notification.event.NotificationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final NotificationEventPublisher notificationEventPublisher;
    private final StudentExamRegistrationRepository studentExamRegistrationRepository;

    public ExamServiceImpl(
            ExamRepository examRepository,
            ExamMapper examMapper, NotificationEventPublisher notificationEventPublisher, StudentExamRegistrationRepository studentExamRegistrationRepository) {

        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.notificationEventPublisher = notificationEventPublisher;
        this.studentExamRegistrationRepository = studentExamRegistrationRepository;
    }

    @Override
    public ExamResponse create(ExamRequest request) {

        validateExamRequest(request, null);

        Exam exam = examMapper.toEntity(request);

        if (exam.getActive() == null) {
            exam.setActive(true);
        }

        Exam savedExam = examRepository.save(exam);
        /*
         * Get students registered for this exam.
         */
        List<StudentExamRegistration> studentIds =
                studentExamRegistrationRepository
                        .findByExamIdAndActiveTrue(
                                savedExam.getId()
                        );

        /*
         * Publish notification for every registered student.
         */
        for (StudentExamRegistration  registration : studentIds) {

            notificationEventPublisher.publish(
                    NotificationEvent.builder()
                            .userId(registration
                                    .getStudentId())
                            .organizationId(
                                    savedExam.getOrganizationId()
                            )
                            .title("Exam Scheduled")
                            .message(
                                    savedExam.getName()
                                            + " has been scheduled."
                            )
                            .type(
                                    NotificationType.EXAM
                            )
                            .build()
            );
        }

        return examMapper.toResponse(savedExam);
    }

    @Override
    public ExamResponse getById(Long id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam not found with id : " + id
                        )
                );

        return mapToResponse(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAll() {

        return examMapper.toResponseList(
                examRepository.findAll()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByOrganizationId(
            Long organizationId) {

        return examMapper.toResponseList(
                examRepository
                        .findByOrganizationIdAndActiveTrue(
                                organizationId
                        )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByAcademicSessionId(
            Long academicSessionId) {

        return examMapper.toResponseList(
                examRepository
                        .findByAcademicSessionIdAndActiveTrue(
                                academicSessionId
                        )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByClassAndSection(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId) {

        return examMapper.toResponseList(
                examRepository
                        .findByAcademicSessionIdAndClassRoomIdAndSectionIdAndActiveTrue(
                                academicSessionId,
                                classRoomId,
                                sectionId
                        )
        );
    }

    @Override
    public ExamResponse update(
            Long id,
            ExamRequest request) {

        Exam exam = findExamById(id);

        validateExamRequest(request, id);

        examMapper.updateEntity(request, exam);

        Exam updatedExam = examRepository.save(exam);

        return examMapper.toResponse(updatedExam);
    }

    @Override
    public void delete(Long id) {

        Exam exam = findExamById(id);

        examRepository.delete(exam);
    }

    @Override
    public ExamResponse activate(Long id) {

        Exam exam = findExamById(id);

        exam.setActive(true);

        return examMapper.toResponse(
                examRepository.save(exam)
        );
    }

    @Override
    public ExamResponse deactivate(Long id) {

        Exam exam = findExamById(id);

        exam.setActive(false);

        return examMapper.toResponse(
                examRepository.save(exam)
        );
    }

    private Exam findExamById(Long id) {

        return examRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Exam not found with id: " + id
                        )
                );
    }

    private void validateExamRequest(
            ExamRequest request,
            Long examId) {

        validateDates(
                request.getStartDate(),
                request.getEndDate()
        );

        validateDuplicateExam(
                request,
                examId
        );

        /*
         * Next validation block:
         *
         * 1. Organization must exist
         * 2. Academic Session must exist
         * 3. ClassRoom must exist
         * 4. Section must exist
         * 5. Academic Session must belong to Organization
         * 6. ClassRoom must belong to Organization
         * 7. Section must belong to Organization
         * 8. Section must belong to selected ClassRoom
         *
         * Add these after connecting the exact repositories
         * from your existing modules.
         */
    }

    private void validateDates(
            java.time.LocalDate startDate,
            java.time.LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }
    }

    private void validateDuplicateExam(
            ExamRequest request,
            Long examId) {

        boolean exists;

        if (examId == null) {

            exists = examRepository
                    .existsByNameIgnoreCaseAndAcademicSessionIdAndClassRoomIdAndSectionId(
                            request.getName(),
                            request.getAcademicSessionId(),
                            request.getClassRoomId(),
                            request.getSectionId()
                    );

        } else {

            exists = examRepository
                    .existsByNameIgnoreCaseAndAcademicSessionIdAndClassRoomIdAndSectionIdAndIdNot(
                            request.getName(),
                            request.getAcademicSessionId(),
                            request.getClassRoomId(),
                            request.getSectionId(),
                            examId
                    );
        }

        if (exists) {
            throw new IllegalArgumentException(
                    "Exam already exists with the same name, "
                            + "academic session, class room and section."
            );
        }
    }

    @Override
    public Page<ExamResponse> getPage(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return examRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }

    private ExamResponse mapToResponse(Exam exam) {

        return ExamResponse.builder()
                .id(exam.getId())
                .organizationId(exam.getOrganizationId())
                .academicSessionId(exam.getAcademicSessionId())
                .classRoomId(exam.getClassRoomId())
                .sectionId(exam.getSectionId())
                .name(exam.getName())
                .examType(String.valueOf(exam.getExamType()))
                .startDate(exam.getStartDate())
                .endDate(exam.getEndDate())
                .status(String.valueOf(exam.getStatus()))
                .remarks(exam.getRemarks())
                .active(exam.getActive())
                .createdAt(exam.getCreatedAt())
                .updatedAt(exam.getUpdatedAt())
                .build();
    }


}