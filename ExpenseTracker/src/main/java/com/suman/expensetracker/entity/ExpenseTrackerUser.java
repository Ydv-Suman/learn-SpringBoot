package com.suman.expensetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "users")
public class ExpenseTrackerUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(max=50)
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Size(max=70)
    @NotNull
    @Column(name =  "EMAIL", nullable = false)
    private String email;

    @Size(max=200)
    @NotNull
    @Column(name = "HASHED_PASSWORD", nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "expenseTrackerUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseList> expenseLists = new ArrayList<>();

    public void addExpense(ExpenseList expenseList) {
        expenseLists.add(expenseList);
        expenseList.setExpenseTrackerUser(this);
    }

    public void removeExpense(ExpenseList expenseList) {
        expenseLists.remove(expenseList);
        expenseList.setExpenseTrackerUser(null);
    }
}
