package com.suman.expensetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "expense_category")
public class ExpenseCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(max=50)
    @NotNull
    @Column(name = "CATEGORY_NAME", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "expenseCategory")
    private List<ExpenseList> expenseLists = new ArrayList<>();
}
