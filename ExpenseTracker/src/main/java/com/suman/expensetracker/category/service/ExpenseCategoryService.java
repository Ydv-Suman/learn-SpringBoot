package com.suman.expensetracker.category.service;

import com.suman.expensetracker.dto.ExpenseCategoryRequestDto;
import com.suman.expensetracker.dto.ExpenseCategoryResponseDto;

import java.util.List;

public interface ExpenseCategoryService {
    ExpenseCategoryResponseDto addCategory(ExpenseCategoryRequestDto requestDto);

    List<ExpenseCategoryResponseDto> getAllCategories();
}
