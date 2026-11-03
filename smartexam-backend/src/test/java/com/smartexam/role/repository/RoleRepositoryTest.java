/*
package com.smartexam.role.repository;

import com.smartexam.role.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    void shouldFindRoleByName() {

        Optional<Role> role =
                repository.findByName("ADMIN");
        assertTrue(role.isPresent());

    }

}*/
