package com.smartexam.organization.service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrganizationServiceTest {

    @Autowired
    private OrganizationService service;

    @Test
    void shouldLoadService() {

        assertNotNull(service);

    }

}