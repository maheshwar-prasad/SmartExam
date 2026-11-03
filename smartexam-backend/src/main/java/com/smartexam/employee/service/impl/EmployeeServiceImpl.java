package com.smartexam.employee.service.impl;

import com.smartexam.employee.dto.EmployeeRequest;
import com.smartexam.employee.dto.EmployeeResponse;
import com.smartexam.employee.entity.Employee;
import com.smartexam.employee.enums.EmployeeType;
import com.smartexam.employee.mapper.EmployeeMapper;
import com.smartexam.employee.repository.EmployeeRepository;
import com.smartexam.employee.service.EmployeeService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.common.exception.DuplicateResourceException;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        validateEmployeeCode(
                request.getOrganizationId(),
                request.getEmployeeCode(),
                null
        );

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found: " + request.getOrganizationId()
                ));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: " + request.getUserId()
                ));

        if (employeeRepository.existsByOrganization_IdAndUser_Id(
                request.getOrganizationId(),
                request.getUserId())) {
            throw new IllegalArgumentException(
                    "This user is already registered as an employee "
                            + "in this organization"
            );
        }

        Employee employee = new Employee();
        copyRequestToEntity(request, employee);
        employee.setOrganization(organization);
        employee.setUser(user);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeRequest request) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + employeeId
                ));

        validateEmployeeCode(
                request.getOrganizationId(),
                request.getEmployeeCode(),
                employeeId
        );

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization not found: "
                                + request.getOrganizationId()
                ));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization not found: "
                                + request.getOrganizationId()
                ));

        employeeRepository
                .findByOrganization_IdAndUser_Id(
                        request.getOrganizationId(),
                        request.getUserId()
                )
                .filter(existing -> !existing.getId().equals(employeeId))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "This user is already registered as another employee"
                    );
                });

        copyRequestToEntity(request, employee);
        employee.setOrganization(organization);
        employee.setUser(user);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found: " + employeeId
                ));

        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getEmployees(
            Long organizationId,
            Pageable pageable) {

        return employeeRepository
                .findByOrganization_Id(organizationId, pageable)
                .map(employeeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> searchEmployees(
            Long organizationId,
            String search,
            EmployeeType employeeType,
            Boolean active,
            Pageable pageable) {

        String normalizedSearch = search == null || search.isBlank()
                ? null
                : search.trim().toLowerCase(Locale.ROOT);

        List<Employee> employees = employeeRepository
                .findByOrganization_Id(organizationId);

        List<EmployeeResponse> filtered = employees.stream()
                .filter(e -> normalizedSearch == null
                        || contains(e.getEmployeeCode(), normalizedSearch)
                        || contains(e.getFirstName(), normalizedSearch)
                        || contains(e.getLastName(), normalizedSearch)
                        || contains(e.getEmail(), normalizedSearch)
                        || contains(e.getMobile(), normalizedSearch))
                .filter(e -> employeeType == null
                        || e.getEmployeeType() == employeeType)
                .filter(e -> active == null
                        || active.equals(e.getActive()))
                .map(employeeMapper::toResponse)
                .toList();

        Sort sort = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by(Sort.Direction.ASC, "firstName");

        List<EmployeeResponse> sorted = filtered.stream()
                .sorted((a, b) -> {
                    int result = compareIgnoreCase(
                            a.getFirstName(), b.getFirstName()
                    );
                    return result != 0
                            ? result
                            : compareIgnoreCase(
                            a.getEmployeeCode(), b.getEmployeeCode()
                    );
                })
                .toList();

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int from = Math.min(page * size, sorted.size());
        int to = Math.min(from + size, sorted.size());

        return new PageImpl<>(
                sorted.subList(from, to),
                PageRequest.of(page, size, sort),
                sorted.size()
        );
    }

    @Override
    public void deactivateEmployee(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found: " + employeeId
                ));

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private void validateEmployeeCode(
            Long organizationId,
            String employeeCode,
            Long currentEmployeeId) {

        employeeRepository
                .findByOrganization_IdAndEmployeeCode(
                        organizationId,
                        employeeCode.trim()
                )
                .filter(existing -> !existing.getId().equals(currentEmployeeId))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Employee code already exists in this organization",existing.getEmployeeCode()
                    );
                });
    }

    private void copyRequestToEntity(
            EmployeeRequest request,
            Employee employee) {

        employee.setEmployeeCode(request.getEmployeeCode().trim());
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setEmployeeType(request.getEmployeeType());
        employee.setGender(request.getGender());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setEmail(normalize(request.getEmail()));
        employee.setMobile(normalize(request.getMobile()));
        employee.setAddress(normalize(request.getAddress()));

        if (request.getActive() != null) {
            employee.setActive(request.getActive());
        }
    }

    private boolean contains(String value, String search) {
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(search);
    }

    private int compareIgnoreCase(String first, String second) {
        String a = first == null ? "" : first;
        String b = second == null ? "" : second;
        return a.compareToIgnoreCase(b);
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}