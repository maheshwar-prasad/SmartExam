package com.smartexam.user.service;

import com.smartexam.user.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse update(Long id, UserUpdateRequest request);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    Page<UserResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<UserResponse> search(
            String keyword,
            int page,
            int size
    );

    UserResponse activate(Long id);

    UserResponse deactivate(Long id);

    void delete(Long id);

    void changePassword(
            Long userId,
            ChangePasswordRequest request
    );

}