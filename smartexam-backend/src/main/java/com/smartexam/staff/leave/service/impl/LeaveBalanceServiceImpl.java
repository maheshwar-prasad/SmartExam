package com.smartexam.staff.leave.service.impl;
import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.staff.leave.dto.LeaveBalanceRequest;
import com.smartexam.staff.leave.dto.LeaveBalanceResponse;
import com.smartexam.staff.leave.dto.LeaveBalanceUpdateRequest;
import com.smartexam.staff.leave.entity.LeaveBalance;
import com.smartexam.staff.leave.entity.LeaveType;
import com.smartexam.staff.leave.mapper.LeaveBalanceMapper;
import com.smartexam.staff.leave.repository.LeaveBalanceRepository;
import com.smartexam.staff.leave.repository.LeaveTypeRepository;
import com.smartexam.staff.leave.service.LeaveBalanceService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.teacher.entity.Teacher;
import com.smartexam.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveBalanceServiceImpl
        implements LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveBalanceMapper leaveBalanceMapper;
    private final OrganizationRepository organizationRepository;
    private final TeacherRepository teacherRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final AcademicSessionRepository academicSessionRepository;


    @Override
    public LeaveBalanceResponse create(
            LeaveBalanceRequest request
    ) {

        validateDuplicate(
                request.organizationId(),
                request.teacherId(),
                request.leaveTypeId(),
                request.academicSessionId(),
                null
        );

        Organization organization =
                organizationRepository.findById(
                        request.organizationId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Organization not found: "
                                        + request.organizationId()
                        )
                );

        Teacher teacher =
                teacherRepository.findById(
                        request.teacherId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Teacher not found: "
                                        + request.teacherId()
                        )
                );

        LeaveType leaveType =
                leaveTypeRepository.findById(
                        request.leaveTypeId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Leave type not found: "
                                        + request.leaveTypeId()
                        )
                );

        AcademicSession academicSession =
                academicSessionRepository.findById(
                        request.academicSessionId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Academic session not found: "
                                        + request.academicSessionId()
                        )
                );

        validateOrganizationRelationships(
                organization,
                teacher,
                leaveType,
                academicSession
        );

        LeaveBalance entity =
                leaveBalanceMapper.toEntity(request);

        entity.setOrganization(organization);
        entity.setTeacher(teacher);
        entity.setLeaveType(leaveType);
        entity.setAcademicSession(academicSession);

        applyDefaults(entity);

        LeaveBalance saved =
                leaveBalanceRepository.save(entity);

        return leaveBalanceMapper.toResponse(saved);
    }


    @Override
    public LeaveBalanceResponse update(
            Long id,
            LeaveBalanceUpdateRequest request
    ) {

        LeaveBalance entity =
                leaveBalanceRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave balance not found: " + id
                                )
                        );

        Long organizationId =
                request.organizationId() != null
                        ? request.organizationId()
                        : entity.getOrganization().getId();

        Long teacherId =
                request.teacherId() != null
                        ? request.teacherId()
                        : entity.getTeacher().getId();

        Long leaveTypeId =
                request.leaveTypeId() != null
                        ? request.leaveTypeId()
                        : entity.getLeaveType().getId();

        Long academicSessionId =
                request.academicSessionId() != null
                        ? request.academicSessionId()
                        : entity.getAcademicSession().getId();

        validateDuplicate(
                organizationId,
                teacherId,
                leaveTypeId,
                academicSessionId,
                id
        );

        Organization organization =
                organizationRepository.findById(
                        organizationId
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Organization not found: "
                                        + organizationId
                        )
                );

        Teacher teacher =
                teacherRepository.findById(teacherId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Teacher not found: "
                                                + teacherId
                                )
                        );

        LeaveType leaveType =
                leaveTypeRepository.findById(leaveTypeId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave type not found: "
                                                + leaveTypeId
                                )
                        );

        AcademicSession academicSession =
                academicSessionRepository.findById(
                        academicSessionId
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Academic session not found: "
                                        + academicSessionId
                        )
                );

        validateOrganizationRelationships(
                organization,
                teacher,
                leaveType,
                academicSession
        );

        leaveBalanceMapper.updateEntity(
                request,
                entity
        );

        entity.setOrganization(organization);
        entity.setTeacher(teacher);
        entity.setLeaveType(leaveType);
        entity.setAcademicSession(academicSession);

        applyDefaults(entity);

        LeaveBalance updated =
                leaveBalanceRepository.save(entity);

        return leaveBalanceMapper.toResponse(updated);
    }


    @Override
    @Transactional(readOnly = true)
    public LeaveBalanceResponse getById(Long id) {

        LeaveBalance entity =
                leaveBalanceRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave balance not found: " + id
                                )
                        );

        return leaveBalanceMapper.toResponse(entity);
    }


    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalanceResponse> getAll() {

        List<LeaveBalance> entities =
                leaveBalanceRepository.findAll(
                        Sort.by(
                                Sort.Direction.ASC,
                                "id"
                        )
                );

        return leaveBalanceMapper.toResponseList(
                entities
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Page<LeaveBalanceResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection =
                "DESC".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        Page<LeaveBalance> result =
                leaveBalanceRepository.findAll(pageable);

        return result.map(
                leaveBalanceMapper::toResponse
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalanceResponse> getByOrganization(
            Long organizationId
    ) {

        return leaveBalanceMapper.toResponseList(
                leaveBalanceRepository
                        .findByOrganizationId(organizationId)
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalanceResponse> getByTeacher(
            Long teacherId
    ) {

        return leaveBalanceMapper.toResponseList(
                leaveBalanceRepository
                        .findByTeacherId(teacherId)
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalanceResponse> getByTeacherAndSession(
            Long teacherId,
            Long academicSessionId
    ) {

        return leaveBalanceMapper.toResponseList(
                leaveBalanceRepository
                        .findByTeacherIdAndAcademicSessionId(
                                teacherId,
                                academicSessionId
                        )
        );
    }


    @Override
    public LeaveBalanceResponse activate(Long id) {

        LeaveBalance entity =
                leaveBalanceRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave balance not found: " + id
                                )
                        );

        entity.setActive(true);

        return leaveBalanceMapper.toResponse(
                leaveBalanceRepository.save(entity)
        );
    }


    @Override
    public LeaveBalanceResponse deactivate(Long id) {

        LeaveBalance entity =
                leaveBalanceRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave balance not found: " + id
                                )
                        );

        entity.setActive(false);

        return leaveBalanceMapper.toResponse(
                leaveBalanceRepository.save(entity)
        );
    }


    @Override
    public void delete(Long id) {

        LeaveBalance entity =
                leaveBalanceRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave balance not found: " + id
                                )
                        );

        leaveBalanceRepository.delete(entity);
    }


    private void validateDuplicate(
            Long organizationId,
            Long teacherId,
            Long leaveTypeId,
            Long academicSessionId,
            Long id
    ) {

        boolean exists;

        if (id == null) {

            exists =
                    leaveBalanceRepository
                            .existsByOrganizationIdAndTeacherIdAndLeaveTypeIdAndAcademicSessionId(
                                    organizationId,
                                    teacherId,
                                    leaveTypeId,
                                    academicSessionId
                            );

        } else {

            exists =
                    leaveBalanceRepository
                            .existsByOrganizationIdAndTeacherIdAndLeaveTypeIdAndAcademicSessionIdAndIdNot(
                                    organizationId,
                                    teacherId,
                                    leaveTypeId,
                                    academicSessionId,
                                    id
                            );
        }

        if (exists) {
            throw new IllegalArgumentException(
                    "Leave balance already exists for the selected "
                            + "teacher, leave type and academic session."
            );
        }
    }


    private void validateOrganizationRelationships(
            Organization organization,
            Teacher teacher,
            LeaveType leaveType,
            AcademicSession academicSession
    ) {

        Long organizationId = organization.getId();

        if (teacher.getOrganization() == null
                || !organizationId.equals(
                teacher.getOrganization().getId()
        )) {

            throw new IllegalArgumentException(
                    "Teacher does not belong to Organization."
            );
        }

        if (leaveType.getOrganization() == null
                || !organizationId.equals(
                leaveType.getOrganization().getId()
        )) {

            throw new IllegalArgumentException(
                    "Leave Type does not belong to Organization."
            );
        }

        if (academicSession.getOrganization() == null
                || !organizationId.equals(
                academicSession.getOrganization().getId()
        )) {

            throw new IllegalArgumentException(
                    "Academic Session does not belong to Organization."
            );
        }
    }


    private void applyDefaults(
            LeaveBalance entity
    ) {

        if (entity.getAllocatedDays() == null) {
            entity.setAllocatedDays(BigDecimal.ZERO);
        }

        if (entity.getUsedDays() == null) {
            entity.setUsedDays(BigDecimal.ZERO);
        }

        if (entity.getPendingDays() == null) {
            entity.setPendingDays(BigDecimal.ZERO);
        }

        if (entity.getRemainingDays() == null) {

            entity.setRemainingDays(
                    entity.getAllocatedDays()
                            .subtract(entity.getUsedDays())
                            .subtract(entity.getPendingDays())
            );
        }

        if (entity.getActive() == null) {
            entity.setActive(true);
        }
    }
}