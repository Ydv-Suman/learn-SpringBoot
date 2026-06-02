package com.eazybytes.jobportal.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactRequestDtoValidationTests {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Test
    void rejectsLowercaseJobSeekerValue() {
        ContactRequestDto dto = new ContactRequestDto(
                "test@example.com",
                "This is a valid contact message.",
                "John Smith",
                "Need help with my account",
                "jobseeker"
        );

        Set<ConstraintViolation<ContactRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertTrue(
                violations.iterator().next().getMessage().contains("Job Seeker"),
                "Expected the validation message to explain the allowed values"
        );
    }

    @Test
    void acceptsCanonicalJobSeekerValue() {
        ContactRequestDto dto = new ContactRequestDto(
                "test@example.com",
                "This is a valid contact message.",
                "John Smith",
                "Need help with my account",
                "Job Seeker"
        );

        Set<ConstraintViolation<ContactRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected Job Seeker to pass validation");
    }

    @Test
    void rejectsUnsupportedUserType() {
        ContactRequestDto dto = new ContactRequestDto(
                "test@example.com",
                "This is a valid contact message.",
                "John Smith",
                "Need help with my account",
                "Teacher"
        );

        Set<ConstraintViolation<ContactRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertTrue(
                violations.iterator().next().getMessage().contains("Job Seeker"),
                "Expected the validation message to explain the allowed values"
        );
    }
}
