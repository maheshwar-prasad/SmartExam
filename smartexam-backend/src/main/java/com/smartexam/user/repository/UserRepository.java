package com.smartexam.user.repository;

import com.smartexam.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u
            FROM User u
            JOIN FETCH u.role r
            LEFT JOIN FETCH r.permissions
            WHERE u.email = :email
            """)
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByMobile(String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
    /**
     * Active Users
     */
    List<User> findByActiveTrue();

    /**
     * Active Users Pagination
     */
    Page<User> findByActiveTrue(Pageable pageable);

    /**
     * Organization Users
     */
    Page<User> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    /**
     * Role Users
     */
    Page<User> findByRoleId(
            Long roleId,
            Pageable pageable
    );

    /**
     * Search User
     */
    @Query("""
            SELECT u
            FROM User u
            WHERE
            LOWER(u.firstName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR
            LOWER(u.lastName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR
            LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR
            LOWER(u.mobile) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    Page<User> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Login Validation
     */
    @Query("""
            SELECT COUNT(u)>0
            FROM User u
            WHERE u.email=:email
            AND u.enabled=true
            """)
    boolean existsActiveUser(
            @Param("email") String email
    );

}