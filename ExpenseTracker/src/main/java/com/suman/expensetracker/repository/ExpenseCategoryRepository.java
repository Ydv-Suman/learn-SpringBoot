package com.suman.expensetracker.repository;

import com.suman.expensetracker.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    Optional<ExpenseCategory> findByExpenseTrackerUser_EmailAndCategoryNameIgnoreCase(String email, String categoryName);

    List<ExpenseCategory> findAllByExpenseTrackerUser_EmailOrderByCategoryNameAsc(String email);
}
