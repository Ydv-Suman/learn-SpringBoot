package com.suman.expensetracker.repository;

import com.suman.expensetracker.entity.ExpenseTrackerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseTrackerUserRepository extends JpaRepository<ExpenseTrackerUser, Long> {
}
