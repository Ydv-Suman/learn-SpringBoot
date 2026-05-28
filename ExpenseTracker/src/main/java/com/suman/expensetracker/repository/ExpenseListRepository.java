package com.suman.expensetracker.repository;

import com.suman.expensetracker.entity.ExpenseList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseListRepository extends JpaRepository<ExpenseList, Long> {

    Optional<ExpenseList> findByExpenseTrackerUser_EmailAndNameIgnoreCase(String email, String name);

    List<ExpenseList> findAllByExpenseTrackerUser_EmailOrderByNameAsc(String email);



}
