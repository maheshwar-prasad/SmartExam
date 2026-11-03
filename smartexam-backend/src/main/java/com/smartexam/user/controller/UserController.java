package com.smartexam.user.controller;

import com.smartexam.user.dto.ChangePasswordRequest;
import com.smartexam.user.dto.UserRequest;
import com.smartexam.user.dto.UserResponse;
import com.smartexam.user.dto.UserUpdateRequest;
import com.smartexam.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "User Management APIs")
public class UserController {

    private final UserService userService;

    /**
     * Create User
     */
    @PostMapping
    @Operation(summary = "Create User")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    /**
     * Update User
     */
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.update(id, request);
    }

    /**
     * Get User By Id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get User By Id")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getById(id));
    }

    /**
     * Get All Users
     */
    @GetMapping
    @Operation(summary = "Get All Users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAll());
    }

    /**
     * Pagination
     */
    @GetMapping("/page")
    @Operation(summary = "User Pagination")
    public ResponseEntity<Page<UserResponse>> getUsers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "firstName")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return ResponseEntity.ok(
                userService.getAll(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    /**
     * Search Users
     */
    @GetMapping("/search")
    @Operation(summary = "Search Users")
    public ResponseEntity<Page<UserResponse>> searchUsers(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                userService.search(keyword, page, size));
    }

    /**
     * Activate User
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate User")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.activate(id));
    }

    /**
     * Deactivate User
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate User")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.deactivate(id));
    }

    /**
     * Change Password
     */
    @PatchMapping("/{id}/change-password")
    @Operation(summary = "Change Password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(id, request);

        return ResponseEntity.noContent().build();
    }

    /**
     * Soft Delete User
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

}