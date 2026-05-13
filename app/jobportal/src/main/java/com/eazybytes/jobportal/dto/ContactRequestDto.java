package com.eazybytes.jobportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ContactRequestDto(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Message is required")
        @Size(min = 10, max=500, message = "Message must be between 10 and 500 characters")
        String message,

        @NotBlank(message = "Name is required")
        @Size(min = 3, max=50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Subject is required")
        @Size(min = 3, max=150, message = "Subject must be between 3 and 150 characters")
        String subject,

        @NotBlank(message = "User Type is required")
        @Pattern(regexp = "Employer|Job Seeker|Other", message = "Invalid user type")
        String userType)
        implements Serializable {
}