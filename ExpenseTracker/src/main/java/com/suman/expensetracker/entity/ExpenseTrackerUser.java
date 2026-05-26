package com.suman.expensetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "USERS")
public class ExpenseTrackerUser {

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
    @Column(name =  "Email", nullable = false)
    private String email;

    @Size(max=200)
    @NotNull
    @Column(name = "HASHED_PASSWORD", nullable = false)
    private String passwordHash;
}
