package com.suman.expensetracker.expenseList.service;

import com.suman.expensetracker.dto.ExpenseListRequestDto;
import com.suman.expensetracker.dto.ExpenseListOverviewResponseDto;
import com.suman.expensetracker.dto.ExpenseListResponseDto;

import java.util.List;

public interface IExpenseListService {

    ExpenseListOverviewResponseDto getAllExpenseLists();

    void addExpenseList(ExpenseListRequestDto requestDto);

    void updateExpenseList(Long id, ExpenseListRequestDto requestDto);

    void deleteExpenseList(Long id);

}
