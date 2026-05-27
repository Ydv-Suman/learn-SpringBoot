package com.suman.expensetracker.category;

import com.suman.expensetracker.category.service.ExpenseCategoryService;
import com.suman.expensetracker.dto.ExpenseCategoryRequestDto;
import com.suman.expensetracker.dto.ExpenseCategoryResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseCategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(expenseCategoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<ExpenseCategoryResponseDto> addCategory(@Valid @RequestBody ExpenseCategoryRequestDto requestDto) {
        ExpenseCategoryResponseDto responseDto = expenseCategoryService.addCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
