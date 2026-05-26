package com.suman.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Name is missing")
        @Size(max = 50)
        String name,

        @NotBlank(message = "Email is missing")
        @Size(max = 70)
        String email,

        @NotBlank(message = "Password is missing")
        @Size(min = 6, max = 200)
        String password
) {
}