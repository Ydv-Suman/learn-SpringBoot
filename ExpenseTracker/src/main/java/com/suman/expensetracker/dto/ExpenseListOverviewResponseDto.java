package com.suman.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseListOverviewResponseDto(BigDecimal totalAmount, List<ExpenseListResponseDto> expenseLists) {
}
