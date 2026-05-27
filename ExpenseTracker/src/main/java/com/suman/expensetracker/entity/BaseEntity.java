package com.suman.expensetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UPDATED_AT",insertable = false)
    private Instant updatedAt;
}
