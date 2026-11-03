package com.smartexam.user.repository;

import com.smartexam.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    void shouldFindByEmail() {

        Optional<User> user =
                repository.findByEmail(
                        "admin@smartexam.com"
                );

        assertTrue(user.isPresent());

    }

}