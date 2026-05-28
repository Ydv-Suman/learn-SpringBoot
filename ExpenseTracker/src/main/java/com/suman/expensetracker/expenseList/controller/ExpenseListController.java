package com.suman.expensetracker.expenseList.controller;

import com.suman.expensetracker.dto.ExpenseListRequestDto;
import com.suman.expensetracker.dto.ExpenseListOverviewResponseDto;
import com.suman.expensetracker.expenseList.service.IExpenseListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expense-lists")
@RequiredArgsConstructor
public class ExpenseListController {

    private final IExpenseListService expenseListService;

    @GetMapping("/all")
    public ResponseEntity<ExpenseListOverviewResponseDto> getAllExpenseLists() {
        return ResponseEntity.ok(expenseListService.getAllExpenseLists());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addExpenseList(@Valid @RequestBody ExpenseListRequestDto requestDto) {
        expenseListService.addExpenseList(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
