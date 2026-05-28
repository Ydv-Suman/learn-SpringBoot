package com.suman.expensetracker.category.service;

import com.suman.expensetracker.dto.ExpenseCategoryRequestDto;
import com.suman.expensetracker.dto.ExpenseCategoryResponseDto;

import java.util.List;

public interface IExpenseCategoryService {

    List<ExpenseCategoryResponseDto> getAllCategories();

    void addCategory(ExpenseCategoryRequestDto requestDto);

    void updateCategory(Long id, ExpenseCategoryRequestDto requestDto);

    void deleteCategory(Long id);

}
