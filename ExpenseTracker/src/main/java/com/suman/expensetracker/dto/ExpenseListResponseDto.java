package com.suman.expensetracker.dto;

import java.math.BigDecimal;

public record ExpenseListResponseDto(Long id, String listName, BigDecimal amount, Long categoryId, String category) {
}
