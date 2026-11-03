package com.smartexam.staff.directory.service.impl;

import com.smartexam.staff.directory.dto.StaffCreateRequest;
import com.smartexam.staff.directory.dto.StaffPageResponse;
import com.smartexam.staff.directory.dto.StaffResponse;
import com.smartexam.staff.directory.dto.StaffUpdateRequest;
import com.smartexam.staff.directory.entity.Staff;
import com.smartexam.staff.directory.enums.StaffStatus;
import com.smartexam.staff.directory.exception.DuplicateStaffException;
import com.smartexam.staff.directory.exception.StaffNotFoundException;
import com.smartexam.staff.directory.mapper.StaffMapper;
import com.smartexam.staff.directory.repository.StaffRepository;
import com.smartexam.staff.directory.service.StaffPhotoStorageService;
import com.smartexam.staff.directory.service.StaffService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class StaffServiceImpl
        implements StaffService {

    private final StaffRepository staffRepository;

    private final StaffMapper staffMapper;

    private final StaffPhotoStorageService
            staffPhotoStorageService;

    public StaffServiceImpl(
            StaffRepository staffRepository,
            StaffMapper staffMapper,
            StaffPhotoStorageService staffPhotoStorageService
    ) {
        this.staffRepository =
                staffRepository;

        this.staffMapper =
                staffMapper;

        this.staffPhotoStorageService =
                staffPhotoStorageService;
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public StaffResponse createStaff(
            Long organizationId,
            StaffCreateRequest request,
            MultipartFile photo
    ) {

        validateCreateRequest(
                organizationId,
                request
        );

        Staff staff =
                staffMapper.toEntity(request);

        staff.setOrganizationId(
                organizationId
        );

        /*
         * New staff starts as ACTIVE.
         */
        staff.setStatus(
                StaffStatus.ACTIVE
        );

        staff.setActive(true);

        /*
         * Save photo if provided.
         */
        if (photo != null &&
                !photo.isEmpty()) {

            String photoUrl =
                    storePhoto(
                            photo,
                            organizationId
                    );

            staff.setProfilePhotoUrl(
                    photoUrl
            );
        }

        Staff savedStaff =
                staffRepository.save(staff);

        return staffMapper.toResponse(
                savedStaff
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public StaffResponse updateStaff(
            Long organizationId,
            Long staffId,
            StaffUpdateRequest request,
            MultipartFile photo
    ) {

        Staff staff =
                findStaff(
                        organizationId,
                        staffId
                );

        validateUpdateRequest(
                organizationId,
                staffId,
                request
        );

        /*
         * Update editable fields.
         */
        staffMapper.updateEntity(
                request,
                staff
        );

        /*
         * Status and active are NOT changed here.
         *
         * They are controlled by activate/deactivate APIs.
         */

        /*
         * Replace photo only when a new photo
         * was uploaded.
         */
        if (photo != null &&
                !photo.isEmpty()) {

            String oldPhotoUrl =
                    staff.getProfilePhotoUrl();

            String newPhotoUrl =
                    storePhoto(
                            photo,
                            organizationId
                    );

            staff.setProfilePhotoUrl(
                    newPhotoUrl
            );

            /*
             * Delete old physical file after
             * successfully storing the new one.
             */
            if (oldPhotoUrl != null &&
                    !oldPhotoUrl.isBlank()) {

                staffPhotoStorageService.delete(
                        oldPhotoUrl
                );
            }
        }

        Staff updatedStaff =
                staffRepository.save(staff);

        return staffMapper.toResponse(
                updatedStaff
        );
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional
    public StaffResponse getStaffById(
            Long organizationId,
            Long staffId
    ) {

        Staff staff =
                findStaff(
                        organizationId,
                        staffId
                );

        return staffMapper.toResponse(
                staff
        );
    }

    // =========================================================
    // GET PAGE
    // =========================================================

    @Override
    @Transactional
    public StaffPageResponse getStaffPage(
            Long organizationId,
            int page,
            int size,
            String keyword,
            StaffStatus status,
            Boolean active
    ) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        /*
         * Prevent excessively large pages.
         */
        size = Math.min(size, 100);

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.ASC,
                                "firstName"
                        )
                );

        String normalizedKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();

        Page<Staff> staffPage;

        boolean hasKeyword =
                !normalizedKeyword.isBlank();

        /*
         * Search + status + active
         */
        if (hasKeyword &&
                status != null &&
                active != null) {

            staffPage =
                    staffRepository
                            .searchByOrganizationIdAndStatusAndActive(
                                    organizationId,
                                    normalizedKeyword,
                                    status,
                                    active,
                                    pageable
                            );

        }
        /*
         * Search + status
         */
        else if (hasKeyword &&
                status != null) {

            staffPage =
                    staffRepository
                            .searchByOrganizationIdAndStatus(
                                    organizationId,
                                    normalizedKeyword,
                                    status,
                                    pageable
                            );

        }
        /*
         * Search + active
         */
        else if (hasKeyword &&
                active != null) {

            staffPage =
                    staffRepository
                            .searchByOrganizationIdAndActive(
                                    organizationId,
                                    normalizedKeyword,
                                    active,
                                    pageable
                            );

        }
        /*
         * Search only
         */
        else if (hasKeyword) {

            staffPage =
                    staffRepository
                            .searchByOrganizationId(
                                    organizationId,
                                    normalizedKeyword,
                                    pageable
                            );

        }
        /*
         * Status + active
         */
        else if (status != null &&
                active != null) {

            staffPage =
                    staffRepository
                            .findByOrganizationIdAndStatusAndActive(
                                    organizationId,
                                    status,
                                    active,
                                    pageable
                            );

        }
        /*
         * Status only
         */
        else if (status != null) {

            staffPage =
                    staffRepository
                            .findByOrganizationIdAndStatus(
                                    organizationId,
                                    status,
                                    pageable
                            );

        }
        /*
         * Active only
         */
        else if (active != null) {

            staffPage =
                    staffRepository
                            .findByOrganizationIdAndActive(
                                    organizationId,
                                    active,
                                    pageable
                            );

        }
        /*
         * All staff
         */
        else {

            staffPage =
                    staffRepository
                            .findByOrganizationId(
                                    organizationId,
                                    pageable
                            );
        }

        return buildPageResponse(
                staffPage
        );
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public StaffResponse activateStaff(
            Long organizationId,
            Long staffId
    ) {

        Staff staff =
                findStaff(
                        organizationId,
                        staffId
                );

        staff.setActive(true);

        staff.setStatus(
                StaffStatus.ACTIVE
        );

        Staff savedStaff =
                staffRepository.save(staff);

        return staffMapper.toResponse(
                savedStaff
        );
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public StaffResponse deactivateStaff(
            Long organizationId,
            Long staffId
    ) {

        Staff staff =
                findStaff(
                        organizationId,
                        staffId
                );

        staff.setActive(false);

        staff.setStatus(
                StaffStatus.INACTIVE
        );

        Staff savedStaff =
                staffRepository.save(staff);

        return staffMapper.toResponse(
                savedStaff
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void deleteStaff(
            Long organizationId,
            Long staffId
    ) {

        Staff staff =
                findStaff(
                        organizationId,
                        staffId
                );

        String photoUrl =
                staff.getProfilePhotoUrl();

        staffRepository.delete(staff);

        /*
         * Remove physical photo after DB deletion.
         */
        if (photoUrl != null &&
                !photoUrl.isBlank()) {

            staffPhotoStorageService.delete(
                    photoUrl
            );
        }
    }

    // =========================================================
    // PRIVATE METHODS
    // =========================================================

    private Staff findStaff(
            Long organizationId,
            Long staffId
    ) {

        return staffRepository
                .findByIdAndOrganizationId(
                        staffId,
                        organizationId
                )
                .orElseThrow(
                        () -> new StaffNotFoundException(
                                staffId,
                                organizationId
                        )
                );
    }

    private void validateCreateRequest(
            Long organizationId,
            StaffCreateRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Staff request cannot be null."
            );
        }

        if (request.getStaffCode() != null &&
                staffRepository
                        .existsByOrganizationIdAndStaffCode(
                                organizationId,
                                request.getStaffCode()
                        )) {

            throw new DuplicateStaffException(
                    "staffCode",
                    request.getStaffCode()
            );
        }

        if (request.getEmployeeCode() != null &&
                !request.getEmployeeCode().isBlank() &&
                staffRepository
                        .existsByOrganizationIdAndEmployeeCode(
                                organizationId,
                                request.getEmployeeCode()
                        )) {

            throw new DuplicateStaffException(
                    "employeeCode",
                    request.getEmployeeCode()
            );
        }

        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                staffRepository
                        .existsByOrganizationIdAndEmail(
                                organizationId,
                                request.getEmail()
                        )) {

            throw new DuplicateStaffException(
                    "email",
                    request.getEmail()
            );
        }
    }

    private void validateUpdateRequest(
            Long organizationId,
            Long staffId,
            StaffUpdateRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Staff request cannot be null."
            );
        }

        if (request.getStaffCode() != null &&
                staffRepository
                        .existsByOrganizationIdAndStaffCodeAndIdNot(
                                organizationId,
                                request.getStaffCode(),
                                staffId
                        )) {

            throw new DuplicateStaffException(
                    "staffCode",
                    request.getStaffCode()
            );
        }

        if (request.getEmployeeCode() != null &&
                !request.getEmployeeCode().isBlank() &&
                staffRepository
                        .existsByOrganizationIdAndEmployeeCodeAndIdNot(
                                organizationId,
                                request.getEmployeeCode(),
                                staffId
                        )) {

            throw new DuplicateStaffException(
                    "employeeCode",
                    request.getEmployeeCode()
            );
        }

        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                staffRepository
                        .existsByOrganizationIdAndEmailAndIdNot(
                                organizationId,
                                request.getEmail(),
                                staffId
                        )) {

            throw new DuplicateStaffException(
                    "email",
                    request.getEmail()
            );
        }
    }

    private String storePhoto(
            MultipartFile photo,
            Long organizationId
    ) {

        try {

            return staffPhotoStorageService.store(
                    photo,
                    organizationId
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Unable to store staff profile photo.",
                    exception
            );
        }
    }

    private StaffPageResponse buildPageResponse(
            Page<Staff> page
    ) {

        StaffPageResponse response =
                new StaffPageResponse();

        response.setContent(
                page.getContent()
                        .stream()
                        .map(
                                staffMapper::toResponse
                        )
                        .toList()
        );

        response.setTotalElements(
                page.getTotalElements()
        );

        response.setTotalPages(
                page.getTotalPages()
        );

        response.setSize(
                page.getSize()
        );

        response.setNumber(
                page.getNumber()
        );

        response.setFirst(
                page.isFirst()
        );

        response.setLast(
                page.isLast()
        );

        response.setNumberOfElements(
                page.getNumberOfElements()
        );

        response.setEmpty(
                page.isEmpty()
        );

        return response;
    }
}