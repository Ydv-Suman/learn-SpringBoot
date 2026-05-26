package com.suman.expensetracker.repository;

import com.suman.expensetracker.entity.ExpenseTrackerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseTrackerUserRepository extends JpaRepository<ExpenseTrackerUser, Long> {
    Optional<ExpenseTrackerUser> readUserByEmail(String email);
    Optional<ExpenseTrackerUser> findByEmail(String email);
}
