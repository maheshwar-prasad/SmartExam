package com.smartexam.gradingsystem.service.impl;

import com.smartexam.gradingsystem.dto.GradingGradeRequest;
import com.smartexam.gradingsystem.dto.GradingSystemRequest;
import com.smartexam.gradingsystem.dto.GradingSystemResponse;
import com.smartexam.gradingsystem.entity.GradingSystem;
import com.smartexam.gradingsystem.entity.GradingSystemGrade;
import com.smartexam.gradingsystem.mapper.GradingSystemMapper;
import com.smartexam.gradingsystem.repository.GradingSystemRepository;
import com.smartexam.gradingsystem.service.GradingSystemService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class GradingSystemServiceImpl implements GradingSystemService {

    private final GradingSystemRepository gradingSystemRepository;
    private final OrganizationRepository organizationRepository;
    private final GradingSystemMapper mapper;

    @Override
    public GradingSystemResponse create(GradingSystemRequest request) {
        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found: " + request.organizationId()
                ));

        String code = normalizeCode(request.code());

        if (gradingSystemRepository.existsByOrganization_IdAndCode(
                request.organizationId(), code)) {
            throw new IllegalArgumentException(
                    "Grading system code already exists: " + code
            );
        }

        validateGrades(request.grades());

        GradingSystem entity = new GradingSystem();
        entity.setOrganization(organization);
        applyRequest(entity, request, code);

        return mapper.toResponse(gradingSystemRepository.save(entity));
    }

    @Override
    public GradingSystemResponse update(Long id, GradingSystemRequest request) {
        GradingSystem entity = gradingSystemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Grading system not found: " + id
                ));

        if (!entity.getOrganization().getId().equals(request.organizationId())) {
            throw new IllegalArgumentException(
                    "Grading system does not belong to the specified organization"
            );
        }

        String code = normalizeCode(request.code());

        if (gradingSystemRepository.existsByOrganization_IdAndCodeAndIdNot(
                request.organizationId(), code, id)) {
            throw new IllegalArgumentException(
                    "Grading system code already exists: " + code
            );
        }

        validateGrades(request.grades());
        applyRequest(entity, request, code);

        return mapper.toResponse(gradingSystemRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public GradingSystemResponse getById(Long id) {
        GradingSystem entity = gradingSystemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Grading system not found: " + id
                ));

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradingSystemResponse> getAll() {
        return gradingSystemRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradingSystemResponse> getByOrganization(Long organizationId) {
        return gradingSystemRepository.findByOrganization_Id(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradingSystemResponse> getActiveByOrganization(Long organizationId) {
        return gradingSystemRepository.findByOrganization_IdAndActiveTrue(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public GradingSystemResponse activate(Long id) {
        GradingSystem entity = findEntity(id);
        entity.setActive(true);
        return mapper.toResponse(gradingSystemRepository.save(entity));
    }

    @Override
    public GradingSystemResponse deactivate(Long id) {
        GradingSystem entity = findEntity(id);
        entity.setActive(false);
        return mapper.toResponse(gradingSystemRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        gradingSystemRepository.delete(findEntity(id));
    }

    private GradingSystem findEntity(Long id) {
        return gradingSystemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Grading system not found: " + id
                ));
    }

    private void applyRequest(
            GradingSystem entity,
            GradingSystemRequest request,
            String code
    ) {
        entity.setName(request.name().trim());
        entity.setCode(code);
        entity.setDescription(request.description());
        entity.setActive(request.active());

        entity.getGrades().clear();

        for (GradingGradeRequest gradeRequest : request.grades()) {
            GradingSystemGrade grade = new GradingSystemGrade();
            grade.setGrade(gradeRequest.grade().trim());
            grade.setMinimumPercentage(gradeRequest.minimumPercentage());
            grade.setMaximumPercentage(gradeRequest.maximumPercentage());
            grade.setGradePoint(gradeRequest.gradePoint());
            grade.setRemarks(gradeRequest.remarks());
            grade.setPassing(gradeRequest.passing());
            grade.setDisplayOrder(gradeRequest.displayOrder());

            entity.addGrade(grade);
        }
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private void validateGrades(List<GradingGradeRequest> grades) {
        Set<String> gradeNames = new HashSet<>();

        for (GradingGradeRequest grade : grades) {
            String normalized = grade.grade().trim().toUpperCase(Locale.ROOT);

            if (!gradeNames.add(normalized)) {
                throw new IllegalArgumentException(
                        "Duplicate grade: " + normalized
                );
            }

            if (grade.minimumPercentage().compareTo(
                    grade.maximumPercentage()) > 0) {
                throw new IllegalArgumentException(
                        "Minimum percentage cannot exceed maximum for grade "
                                + normalized
                );
            }
        }

        List<GradingGradeRequest> sorted = grades.stream()
                .sorted((a, b) -> a.minimumPercentage()
                        .compareTo(b.minimumPercentage()))
                .toList();

        for (int i = 1; i < sorted.size(); i++) {
            BigDecimal previousMaximum = sorted.get(i - 1).maximumPercentage();
            BigDecimal currentMinimum = sorted.get(i).minimumPercentage();

            if (currentMinimum.compareTo(previousMaximum) <= 0) {
                throw new IllegalArgumentException(
                        "Grade percentage ranges must not overlap"
                );
            }
        }
    }
}