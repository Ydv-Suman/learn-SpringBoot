package com.suman.expensetracker.category.service.impl;

import com.suman.expensetracker.category.service.IExpenseCategoryService;
import com.suman.expensetracker.dto.ExpenseCategoryRequestDto;
import com.suman.expensetracker.dto.ExpenseCategoryResponseDto;
import com.suman.expensetracker.entity.ExpenseCategory;
import com.suman.expensetracker.entity.ExpenseTrackerUser;
import com.suman.expensetracker.repository.ExpenseCategoryRepository;
import com.suman.expensetracker.repository.ExpenseTrackerUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements IExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseTrackerUserRepository expenseTrackerUserRepository;

    @Override
    public List<ExpenseCategoryResponseDto> getAllCategories() {
        ExpenseTrackerUser currentUser = getCurrentUser();
        return expenseCategoryRepository.findAllByExpenseTrackerUser_EmailOrderByCategoryNameAsc(currentUser.getEmail())
                .stream()
                .map(category -> new ExpenseCategoryResponseDto(category.getId(), category.getCategoryName()))
                .toList();
    }

    @Override
    public ExpenseCategoryResponseDto addCategory(ExpenseCategoryRequestDto requestDto) {
        ExpenseTrackerUser currentUser = getCurrentUser();
        String normalizedName = requestDto.categoryName().trim();
        expenseCategoryRepository.findByExpenseTrackerUser_EmailAndCategoryNameIgnoreCase(currentUser.getEmail(), normalizedName)
                .ifPresent(category -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
                });

        ExpenseCategory category = new ExpenseCategory();
        category.setCategoryName(normalizedName);
        category.setExpenseTrackerUser(currentUser);
        ExpenseCategory savedCategory = expenseCategoryRepository.save(category);
        return new ExpenseCategoryResponseDto(savedCategory.getId(), savedCategory.getCategoryName());
    }

    @Override
    public ExpenseCategoryResponseDto updateCategory(Long id, ExpenseCategoryRequestDto requestDto) {
        ExpenseTrackerUser currentUser = getCurrentUser();
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
        ExpenseCategory updatedCategory = expenseCategoryRepository.save(category);
        return new ExpenseCategoryResponseDto(updatedCategory.getId(), updatedCategory.getCategoryName());
    }

    @Override
    public void deleteCategory(Long id) {
        ExpenseTrackerUser currentUser = getCurrentUser();
        ExpenseCategory category = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        if (!category.getExpenseTrackerUser().getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        expenseCategoryRepository.delete(category);
    }


    private ExpenseTrackerUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        return expenseTrackerUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
    }
}
