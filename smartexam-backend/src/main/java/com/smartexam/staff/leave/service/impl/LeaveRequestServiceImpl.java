package com.smartexam.staff.leave.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.staff.leave.dto.LeaveRequestRequest;
import com.smartexam.staff.leave.dto.LeaveRequestResponse;
import com.smartexam.staff.leave.dto.LeaveRequestUpdateRequest;
import com.smartexam.staff.leave.entity.LeaveBalance;
import com.smartexam.staff.leave.entity.LeaveRequest;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;
import com.smartexam.staff.leave.entity.LeaveType;
import com.smartexam.staff.leave.mapper.LeaveRequestMapper;
import com.smartexam.staff.leave.repository.LeaveBalanceRepository;
import com.smartexam.staff.leave.repository.LeaveRequestRepository;
import com.smartexam.staff.leave.repository.LeaveTypeRepository;
import com.smartexam.staff.leave.service.LeaveRequestService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;


import com.smartexam.teacher.entity.Teacher;
import com.smartexam.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    private final OrganizationRepository organizationRepository;
    private final TeacherRepository teacherRepository;
    private final AcademicSessionRepository academicSessionRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public LeaveRequestResponse create(
            LeaveRequestRequest request
    ) {

        validateDates(
                request.fromDate(),
                request.toDate()
        );

        Organization organization =
                getOrganization(request.organizationId());

        Teacher teacher =
                getTeacher(request.teacherId());

        LeaveType leaveType =
                getLeaveType(request.leaveTypeId());

        AcademicSession academicSession =
                getAcademicSession(
                        request.academicSessionId()
                );

        validateOrganizationRelationships(
                organization,
                teacher,
                leaveType,
                academicSession
        );

        BigDecimal numberOfDays =
                calculateNumberOfDays(
                        request.fromDate(),
                        request.toDate()
                );

        validateNoOverlappingLeave(
                teacher.getId(),
                request.fromDate(),
                request.toDate(),
                null
        );

        LeaveBalance balance =
                getLeaveBalance(
                        organization.getId(),
                        teacher.getId(),
                        leaveType.getId(),
                        academicSession.getId()
                );

        validateAvailableBalance(
                balance,
                numberOfDays
        );

        LeaveRequest entity =
                leaveRequestMapper.toEntity(request);

        entity.setOrganization(organization);
        entity.setTeacher(teacher);
        entity.setLeaveType(leaveType);
        entity.setAcademicSession(academicSession);

        entity.setNumberOfDays(numberOfDays);
        entity.setStatus(LeaveRequestStatus.PENDING);
        entity.setAppliedAt(LocalDateTime.now());
        entity.setActive(true);

        LeaveRequest saved =
                leaveRequestRepository.save(entity);

        updatePendingDays(
                balance,
                numberOfDays
        );

        leaveBalanceRepository.save(balance);

        return leaveRequestMapper.toResponse(saved);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public LeaveRequestResponse update(
            Long id,
            LeaveRequestUpdateRequest request
    ) {

        LeaveRequest entity =
                getEntity(id);

        validateEditable(entity);

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

        LocalDate fromDate =
                request.fromDate() != null
                        ? request.fromDate()
                        : entity.getFromDate();

        LocalDate toDate =
                request.toDate() != null
                        ? request.toDate()
                        : entity.getToDate();

        validateDates(fromDate, toDate);

        Organization organization =
                getOrganization(organizationId);

        Teacher teacher =
                getTeacher(teacherId);

        LeaveType leaveType =
                getLeaveType(leaveTypeId);

        AcademicSession academicSession =
                getAcademicSession(academicSessionId);

        validateOrganizationRelationships(
                organization,
                teacher,
                leaveType,
                academicSession
        );

        BigDecimal newNumberOfDays =
                calculateNumberOfDays(
                        fromDate,
                        toDate
                );

        /*
         * Return the old pending days before
         * recalculating the balance.
         */
        BigDecimal oldNumberOfDays =
                entity.getNumberOfDays();

        LeaveBalance oldBalance =
                getLeaveBalance(
                        entity.getOrganization().getId(),
                        entity.getTeacher().getId(),
                        entity.getLeaveType().getId(),
                        entity.getAcademicSession().getId()
                );

        if (oldNumberOfDays != null) {

            oldBalance.setPendingDays(
                    safe(oldBalance.getPendingDays())
                            .subtract(oldNumberOfDays)
            );

            recalculateRemainingDays(oldBalance);

            leaveBalanceRepository.save(oldBalance);
        }

        validateNoOverlappingLeave(
                teacher.getId(),
                fromDate,
                toDate,
                id
        );

        LeaveBalance newBalance =
                getLeaveBalance(
                        organization.getId(),
                        teacher.getId(),
                        leaveType.getId(),
                        academicSession.getId()
                );

        validateAvailableBalance(
                newBalance,
                newNumberOfDays
        );

        leaveRequestMapper.updateEntity(
                request,
                entity
        );

        entity.setOrganization(organization);
        entity.setTeacher(teacher);
        entity.setLeaveType(leaveType);
        entity.setAcademicSession(academicSession);

        entity.setFromDate(fromDate);
        entity.setToDate(toDate);
        entity.setNumberOfDays(newNumberOfDays);

        LeaveRequest updated =
                leaveRequestRepository.save(entity);

        newBalance.setPendingDays(
                safe(newBalance.getPendingDays())
                        .add(newNumberOfDays)
        );

        recalculateRemainingDays(newBalance);

        leaveBalanceRepository.save(newBalance);

        return leaveRequestMapper.toResponse(updated);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestResponse getById(Long id) {

        return leaveRequestMapper.toResponse(
                getEntity(id)
        );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getAll() {

        List<LeaveRequest> entities =
                leaveRequestRepository.findAll(
                        Sort.by(
                                Sort.Direction.DESC,
                                "appliedAt"
                        )
                );

        return leaveRequestMapper.toResponseList(
                entities
        );
    }


    // =========================================================
    // PAGINATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getAll(
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

        Page<LeaveRequest> result =
                leaveRequestRepository.findAll(pageable);

        return result.map(
                leaveRequestMapper::toResponse
        );
    }


    // =========================================================
    // ORGANIZATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getByOrganization(
            Long organizationId
    ) {

        return leaveRequestMapper.toResponseList(
                leaveRequestRepository
                        .findByOrganizationId(organizationId)
        );
    }


    // =========================================================
    // TEACHER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getByTeacher(
            Long teacherId
    ) {

        return leaveRequestMapper.toResponseList(
                leaveRequestRepository
                        .findByTeacherId(teacherId)
        );
    }


    // =========================================================
    // TEACHER + SESSION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getByTeacherAndSession(
            Long teacherId,
            Long academicSessionId
    ) {

        return leaveRequestMapper.toResponseList(
                leaveRequestRepository
                        .findByTeacherIdAndAcademicSessionId(
                                teacherId,
                                academicSessionId
                        )
        );
    }


    // =========================================================
    // STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getByStatus(
            Long organizationId,
            LeaveRequestStatus status
    ) {

        return leaveRequestMapper.toResponseList(
                leaveRequestRepository
                        .findByOrganizationIdAndStatus(
                                organizationId,
                                status
                        )
        );
    }


    // =========================================================
    // APPROVE
    // =========================================================

    @Override
    public LeaveRequestResponse approve(
            Long id,
            Long approvedBy,
            String comments
    ) {

        LeaveRequest entity =
                getEntity(id);

        if (entity.getStatus()
                != LeaveRequestStatus.PENDING) {

            throw new IllegalStateException(
                    "Only PENDING leave requests can be approved."
            );
        }

        LeaveBalance balance =
                getLeaveBalance(
                        entity.getOrganization().getId(),
                        entity.getTeacher().getId(),
                        entity.getLeaveType().getId(),
                        entity.getAcademicSession().getId()
                );

        BigDecimal days =
                safe(entity.getNumberOfDays());

        BigDecimal pending =
                safe(balance.getPendingDays());

        if (pending.compareTo(days) < 0) {
            throw new IllegalStateException(
                    "Pending leave balance is insufficient."
            );
        }

        balance.setPendingDays(
                pending.subtract(days)
        );

        balance.setUsedDays(
                safe(balance.getUsedDays())
                        .add(days)
        );

        recalculateRemainingDays(balance);

        leaveBalanceRepository.save(balance);

        entity.setStatus(
                LeaveRequestStatus.APPROVED
        );

        entity.setApprovedBy(approvedBy);
        entity.setApprovedAt(LocalDateTime.now());
        entity.setApprovalComments(comments);

        return leaveRequestMapper.toResponse(
                leaveRequestRepository.save(entity)
        );
    }


    // =========================================================
    // REJECT
    // =========================================================

    @Override
    public LeaveRequestResponse reject(
            Long id,
            Long rejectedBy,
            String comments
    ) {

        LeaveRequest entity =
                getEntity(id);

        if (entity.getStatus()
                != LeaveRequestStatus.PENDING) {

            throw new IllegalStateException(
                    "Only PENDING leave requests can be rejected."
            );
        }

        LeaveBalance balance =
                getLeaveBalance(
                        entity.getOrganization().getId(),
                        entity.getTeacher().getId(),
                        entity.getLeaveType().getId(),
                        entity.getAcademicSession().getId()
                );

        BigDecimal days =
                safe(entity.getNumberOfDays());

        balance.setPendingDays(
                safe(balance.getPendingDays())
                        .subtract(days)
        );

        recalculateRemainingDays(balance);

        leaveBalanceRepository.save(balance);

        entity.setStatus(
                LeaveRequestStatus.REJECTED
        );

        entity.setRejectedBy(rejectedBy);
        entity.setRejectedAt(LocalDateTime.now());
        entity.setApprovalComments(comments);

        return leaveRequestMapper.toResponse(
                leaveRequestRepository.save(entity)
        );
    }


    // =========================================================
    // CANCEL
    // =========================================================

    @Override
    public LeaveRequestResponse cancel(Long id) {

        LeaveRequest entity =
                getEntity(id);

        if (entity.getStatus()
                == LeaveRequestStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Leave request is already cancelled."
            );
        }

        if (entity.getStatus()
                == LeaveRequestStatus.REJECTED) {

            throw new IllegalStateException(
                    "Rejected leave request cannot be cancelled."
            );
        }

        LeaveBalance balance =
                getLeaveBalance(
                        entity.getOrganization().getId(),
                        entity.getTeacher().getId(),
                        entity.getLeaveType().getId(),
                        entity.getAcademicSession().getId()
                );

        BigDecimal days =
                safe(entity.getNumberOfDays());

        if (entity.getStatus()
                == LeaveRequestStatus.PENDING) {

            balance.setPendingDays(
                    safe(balance.getPendingDays())
                            .subtract(days)
            );

        } else if (entity.getStatus()
                == LeaveRequestStatus.APPROVED) {

            balance.setUsedDays(
                    safe(balance.getUsedDays())
                            .subtract(days)
            );
        }

        recalculateRemainingDays(balance);

        leaveBalanceRepository.save(balance);

        entity.setStatus(
                LeaveRequestStatus.CANCELLED
        );

        return leaveRequestMapper.toResponse(
                leaveRequestRepository.save(entity)
        );
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public LeaveRequestResponse activate(Long id) {

        LeaveRequest entity =
                getEntity(id);

        entity.setActive(true);

        return leaveRequestMapper.toResponse(
                leaveRequestRepository.save(entity)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public LeaveRequestResponse deactivate(Long id) {

        LeaveRequest entity =
                getEntity(id);

        entity.setActive(false);

        return leaveRequestMapper.toResponse(
                leaveRequestRepository.save(entity)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void delete(Long id) {

        LeaveRequest entity =
                getEntity(id);

        if (entity.getStatus()
                == LeaveRequestStatus.PENDING) {

            LeaveBalance balance =
                    getLeaveBalance(
                            entity.getOrganization().getId(),
                            entity.getTeacher().getId(),
                            entity.getLeaveType().getId(),
                            entity.getAcademicSession().getId()
                    );

            balance.setPendingDays(
                    safe(balance.getPendingDays())
                            .subtract(
                                    safe(entity.getNumberOfDays())
                            )
            );

            recalculateRemainingDays(balance);

            leaveBalanceRepository.save(balance);
        }

        leaveRequestRepository.delete(entity);
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateDates(
            LocalDate fromDate,
            LocalDate toDate
    ) {

        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException(
                    "From date and To date are required."
            );
        }

        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException(
                    "To date cannot be before From date."
            );
        }
    }


    private BigDecimal calculateNumberOfDays(
            LocalDate fromDate,
            LocalDate toDate
    ) {

        /*
         * Currently counts calendar days inclusively.
         *
         * Example:
         * 10 Oct -> 10 Oct = 1 day
         * 10 Oct -> 12 Oct = 3 days
         */
        long days =
                ChronoUnit.DAYS.between(
                        fromDate,
                        toDate
                ) + 1;

        return BigDecimal.valueOf(days)
                .setScale(2, RoundingMode.HALF_UP);
    }


    private void validateNoOverlappingLeave(
            Long teacherId,
            LocalDate fromDate,
            LocalDate toDate,
            Long currentRequestId
    ) {

        List<LeaveRequest> requests =
                leaveRequestRepository.findByTeacherId(
                        teacherId
                );

        boolean overlap =
                requests.stream()
                        .filter(request ->
                                currentRequestId == null
                                        || !request.getId()
                                        .equals(currentRequestId)
                        )
                        .filter(request ->
                                request.getStatus()
                                        == LeaveRequestStatus.PENDING
                                        || request.getStatus()
                                        == LeaveRequestStatus.APPROVED
                        )
                        .anyMatch(request ->
                                !toDate.isBefore(
                                        request.getFromDate()
                                )
                                        &&
                                        !fromDate.isAfter(
                                                request.getToDate()
                                        )
                        );

        if (overlap) {
            throw new IllegalArgumentException(
                    "Teacher already has a pending or approved "
                            + "leave request for the selected dates."
            );
        }
    }


    private void validateAvailableBalance(
            LeaveBalance balance,
            BigDecimal requestedDays
    ) {

        if (balance == null) {
            throw new IllegalArgumentException(
                    "Leave balance not found for the selected "
                            + "teacher, leave type and academic session."
            );
        }

        if (!Boolean.TRUE.equals(balance.getActive())) {
            throw new IllegalArgumentException(
                    "Leave balance is inactive."
            );
        }

        BigDecimal available =
                safe(balance.getRemainingDays());

        if (available.compareTo(requestedDays) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient leave balance. Available: "
                            + available
                            + ", Requested: "
                            + requestedDays
            );
        }
    }


    private void updatePendingDays(
            LeaveBalance balance,
            BigDecimal days
    ) {

        balance.setPendingDays(
                safe(balance.getPendingDays())
                        .add(days)
        );

        recalculateRemainingDays(balance);
    }


    private void recalculateRemainingDays(
            LeaveBalance balance
    ) {

        BigDecimal allocated =
                safe(balance.getAllocatedDays());

        BigDecimal used =
                safe(balance.getUsedDays());

        BigDecimal pending =
                safe(balance.getPendingDays());

        BigDecimal remaining =
                allocated
                        .subtract(used)
                        .subtract(pending);

        balance.setRemainingDays(
                remaining.max(BigDecimal.ZERO)
        );
    }


    private LeaveBalance getLeaveBalance(
            Long organizationId,
            Long teacherId,
            Long leaveTypeId,
            Long academicSessionId
    ) {

        return leaveBalanceRepository
                .findByOrganizationIdAndTeacherIdAndLeaveTypeIdAndAcademicSessionId(
                        organizationId,
                        teacherId,
                        leaveTypeId,
                        academicSessionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Leave balance not found for the selected "
                                        + "teacher, leave type and academic session."
                        )
                );
    }


    private Organization getOrganization(
            Long id
    ) {

        return organizationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Organization not found: " + id
                        )
                );
    }


    private Teacher getTeacher(
            Long id
    ) {

        return teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Teacher not found: " + id
                        )
                );
    }


    private LeaveType getLeaveType(
            Long id
    ) {

        return leaveTypeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Leave type not found: " + id
                        )
                );
    }


    private AcademicSession getAcademicSession(
            Long id
    ) {

        return academicSessionRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Academic session not found: " + id
                        )
                );
    }


    private LeaveRequest getEntity(Long id) {

        return leaveRequestRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Leave request not found: " + id
                        )
                );
    }


    private void validateOrganizationRelationships(
            Organization organization,
            Teacher teacher,
            LeaveType leaveType,
            AcademicSession academicSession
    ) {

        Long organizationId =
                organization.getId();

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


    private void validateEditable(
            LeaveRequest entity
    ) {

        if (entity.getStatus()
                != LeaveRequestStatus.PENDING) {

            throw new IllegalStateException(
                    "Only PENDING leave requests can be updated."
            );
        }
    }


    private BigDecimal safe(BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }
}
