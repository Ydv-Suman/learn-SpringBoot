package com.suman.expensetracker.expenseList.service.impl;

import com.suman.expensetracker.dto.ExpenseListRequestDto;
import com.suman.expensetracker.dto.ExpenseListOverviewResponseDto;
import com.suman.expensetracker.dto.ExpenseListResponseDto;
import com.suman.expensetracker.entity.ExpenseCategory;
import com.suman.expensetracker.entity.ExpenseList;
import com.suman.expensetracker.entity.ExpenseTrackerUser;
import com.suman.expensetracker.expenseList.service.IExpenseListService;
import com.suman.expensetracker.repository.ExpenseCategoryRepository;
import com.suman.expensetracker.repository.ExpenseListRepository;
import com.suman.expensetracker.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseListServiceImpl implements IExpenseListService {

    private final ExpenseListRepository expenseListRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public ExpenseListOverviewResponseDto getAllExpenseLists() {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        List<ExpenseListResponseDto> expenseLists = expenseListRepository.findAllByExpenseTrackerUser_EmailOrderByNameAsc(currentUser.getEmail())
                .stream()
                .map(expenseList -> new ExpenseListResponseDto(
                        expenseList.getId(),
                        expenseList.getName(),
                        expenseList.getAmount(),
                        expenseList.getExpenseCategory().getId(),
                        expenseList.getExpenseCategory().getCategoryName()
                ))
                .toList();
        BigDecimal totalAmount = expenseLists.stream()
                .map(ExpenseListResponseDto::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ExpenseListOverviewResponseDto(totalAmount, expenseLists);
    }

    @Override
    public void addExpenseList(ExpenseListRequestDto requestDto) {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        String normalizedListName = requestDto.listName().trim();
        expenseListRepository.findByExpenseTrackerUser_EmailAndNameIgnoreCase(currentUser.getEmail(), normalizedListName)
                .ifPresent(expenseList -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expense list already exists");
                });

        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        if (!expenseCategory.getExpenseTrackerUser().getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        ExpenseList expenseList = new ExpenseList();
        expenseList.setName(normalizedListName);
        expenseList.setAmount(requestDto.amount());
        expenseList.setExpenseCategory(expenseCategory);
        expenseList.setExpenseTrackerUser(currentUser);
        expenseListRepository.save(expenseList);
    }


    @Override
    public void updateExpenseList(Long id, ExpenseListRequestDto requestDto) {
        ExpenseTrackerUser currentUser = currentUserService.getCurrentUser();
        ExpenseList expenseList = expenseListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense list not found"));
        if (!expenseList.getExpenseTrackerUser().getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense list not found");
        }

        String normalizedListName = requestDto.listName().trim();
        BigDecimal normalizedAmount = requestDto.amount();
        ExpenseCategory category = expenseCategoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        if (!category.getExpenseTrackerUser().getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        expenseListRepository.findByExpenseTrackerUser_EmailAndNameIgnoreCase(currentUser.getEmail(), normalizedListName)
                .filter(existingExpenseList -> !existingExpenseList.getId().equals(id))
                .ifPresent(existingExpenseList -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expense list already exists");
                });
        expenseList.setName(normalizedListName);
        expenseList.setAmount(normalizedAmount);
        expenseList.setExpenseCategory(category);
        expenseListRepository.save(expenseList);

    }

    @Override
    public void deleteExpenseList(Long id) {

    }
}
