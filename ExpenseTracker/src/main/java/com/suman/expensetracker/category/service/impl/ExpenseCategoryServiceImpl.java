package com.suman.expensetracker.category.service.impl;

import com.suman.expensetracker.category.service.IExpenseCategoryService;
import com.suman.expensetracker.dto.ExpenseCategoryRequestDto;
import com.suman.expensetracker.dto.ExpenseCategoryResponseDto;
import com.suman.expensetracker.entity.ExpenseCategory;
import com.suman.expensetracker.entity.ExpenseTrackerUser;
import com.suman.expensetracker.repository.ExpenseCategoryRepository;
import com.suman.expensetracker.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements IExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<ExpenseCategoryResponseDto> getAllCategories() {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        return expenseCategoryRepository.findAllByExpenseTrackerUser_EmailOrderByCategoryNameAsc(currentUser.getEmail())
                .stream()
                .map(category -> new ExpenseCategoryResponseDto(category.getId(), category.getCategoryName()))
                .toList();
    }

    @Override
    public void addCategory(ExpenseCategoryRequestDto requestDto) {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        String normalizedName = requestDto.categoryName().trim();
        expenseCategoryRepository.findByExpenseTrackerUser_EmailAndCategoryNameIgnoreCase(currentUser.getEmail(), normalizedName)
                .ifPresent(category -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
                });

        ExpenseCategory category = new ExpenseCategory();
        category.setCategoryName(normalizedName);
        category.setExpenseTrackerUser(currentUser);
        expenseCategoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long id, ExpenseCategoryRequestDto requestDto) {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        ExpenseCategory category = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (!category.getExpenseTrackerUser().getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        String normalizedName = requestDto.categoryName().trim();
        expenseCategoryRepository.findByExpenseTrackerUser_EmailAndCategoryNameIgnoreCase(currentUser.getEmail(), normalizedName)
                .filter(existingCategory -> !existingCategory.getId().equals(id))
                .ifPresent(existingCategory -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
                });

        category.setCategoryName(normalizedName);
        expenseCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        ExpenseCategory category = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        if (!category.getExpenseTrackerUser().getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        expenseCategoryRepository.delete(category);
    }
}
