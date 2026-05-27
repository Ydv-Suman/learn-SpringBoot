package com.suman.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExpenseCategoryRequestDto(
        @NotBlank
        @Size(max = 50)
        String categoryName
) {
}
