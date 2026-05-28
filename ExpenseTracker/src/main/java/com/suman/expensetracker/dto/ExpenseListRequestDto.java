package com.suman.expensetracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ExpenseListRequestDto(

        @NotBlank
        @Size(max = 50)
        String listName,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal amount,

        @NotNull
        Long categoryId
) {
}
