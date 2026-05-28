package com.suman.expensetracker.category.controller;

import com.suman.expensetracker.category.service.IExpenseCategoryService;
import com.suman.expensetracker.dto.ExpenseCategoryRequestDto;
import com.suman.expensetracker.dto.ExpenseCategoryResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final IExpenseCategoryService expenseCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseCategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(expenseCategoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<ExpenseCategoryResponseDto> addCategory(@Valid @RequestBody ExpenseCategoryRequestDto requestDto) {
        ExpenseCategoryResponseDto responseDto = expenseCategoryService.addCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseCategoryRequestDto requestDto
    ) {
        return ResponseEntity.ok(expenseCategoryService.updateCategory(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        expenseCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
