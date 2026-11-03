package com.smartexam.staff.directory.service;

import com.smartexam.staff.directory.dto.StaffCreateRequest;
import com.smartexam.staff.directory.dto.StaffPageResponse;
import com.smartexam.staff.directory.dto.StaffResponse;
import com.smartexam.staff.directory.dto.StaffUpdateRequest;
import com.smartexam.staff.directory.enums.StaffStatus;
import org.springframework.web.multipart.MultipartFile;

public interface StaffService {

    /*
     * Create a new staff member.
     */
    StaffResponse createStaff(
            Long organizationId,
            StaffCreateRequest request,
            MultipartFile photo
    );

    /*
     * Update an existing staff member.
     */
    StaffResponse updateStaff(
            Long organizationId,
            Long staffId,
            StaffUpdateRequest request,
            MultipartFile photo
    );

    /*
     * Get one staff member by ID.
     */
    StaffResponse getStaffById(
            Long organizationId,
            Long staffId
    );

    /*
     * Get staff list with pagination, search and filters.
     *
     * keyword:
     * Search by staff code, employee code, name, email,
     * phone and designation.
     *
     * status:
     * Optional staff status filter.
     *
     * active:
     * Optional active/inactive filter.
     */
    StaffPageResponse getStaffPage(
            Long organizationId,
            int page,
            int size,
            String keyword,
            StaffStatus status,
            Boolean active
    );

    /*
     * Activate a staff member.
     */
    StaffResponse activateStaff(
            Long organizationId,
            Long staffId
    );

    /*
     * Deactivate a staff member.
     */
    StaffResponse deactivateStaff(
            Long organizationId,
            Long staffId
    );

    /*
     * Soft delete a staff member.
     */
    void deleteStaff(
            Long organizationId,
            Long staffId
    );
}