package com.smartexam.user.service.impl;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.role.entity.Role;
import com.smartexam.role.repository.RoleRepository;
import com.smartexam.user.dto.*;
import com.smartexam.user.entity.User;
import com.smartexam.user.mapper.UserMapper;
import com.smartexam.user.repository.UserRepository;
import com.smartexam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {

        log.info("Creating User : {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email",
                    request.getEmail());

        }

        if (userRepository.existsByMobile(request.getMobile())) {

            throw new DuplicateResourceException(
                    "Mobile",
                    request.getMobile());

        }

        Organization organization =
                organizationRepository.findById(
                                request.getOrganizationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        request.getOrganizationId()));

        Role role =
                roleRepository.findById(
                                request.getRoleId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Role",
                                        request.getRoleId()));

        User user = userMapper.toEntity(request);

        user.setOrganization(organization);

        user.setRole(role);

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                ));

        user.setActive(true);

        user.setEnabled(true);

        user.setAccountNonExpired(true);

        user.setAccountNonLocked(true);

        user.setCredentialsNonExpired(true);

        User saved = userRepository.save(user);

        log.info("User Created Successfully : {}",
                saved.getId());

        return userMapper.toResponse(saved);

    }

    @Override
    public UserResponse update(Long id,
            UserUpdateRequest request) {

        log.info("Updating User : {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                id));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email",
                    request.getEmail());

        }

        if (!user.getMobile().equals(request.getMobile())
                && userRepository.existsByMobile(request.getMobile())) {

            throw new DuplicateResourceException(
                    "Mobile",
                    request.getMobile());

        }

        userMapper.updateEntity(request, user);

        User updated = userRepository.save(user);

        log.info("User Updated Successfully : {}", updated.getId());

        return userMapper.toResponse(updated);

    }
    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                id));

        return userMapper.toResponse(user);

    }
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {

        return userMapper.toResponseList(
                userRepository.findAll()
        );

    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> search(String keyword,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return userRepository.search(keyword, pageable)
                .map(userMapper::toResponse);

    }

    @Override
    public UserResponse activate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                id));

        user.setActive(true);
        user.setEnabled(true);

        return userMapper.toResponse(
                userRepository.save(user)
        );

    }

    @Override
    public UserResponse deactivate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                id));

        user.setActive(false);
        user.setEnabled(false);

        return userMapper.toResponse(
                userRepository.save(user)
        );

    }

    @Override
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                id));

        user.setActive(false);
        user.setEnabled(false);

        userRepository.save(user);

        log.info("User Soft Deleted : {}", id);

    }

    @Override
    public void changePassword(Long userId,
            ChangePasswordRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                userId));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new BusinessException(
                    "Old password is incorrect.");

        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new BusinessException(
                    "New password and confirm password do not match.");

        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        userRepository.save(user);

    }

}