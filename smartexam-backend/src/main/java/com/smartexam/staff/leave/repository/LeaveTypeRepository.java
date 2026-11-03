package com.smartexam.staff.leave.repository;
import com.smartexam.staff.leave.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveTypeRepository
        extends JpaRepository<LeaveType, Long> {

    List<LeaveType> findByOrganizationId(Long organizationId);

    List<LeaveType> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    Optional<LeaveType> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    Optional<LeaveType> findByOrganizationIdAndCode(
            Long organizationId,
            String code
    );

    boolean existsByOrganizationIdAndCode(
            Long organizationId,
            String code
    );

    boolean existsByOrganizationIdAndCodeAndIdNot(
            Long organizationId,
            String code,
            Long id
    );
}