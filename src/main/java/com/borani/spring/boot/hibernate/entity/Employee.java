package com.borani.spring.boot.hibernate.entity;

import com.borani.spring.boot.hibernate.generator.BigIntegerGeneratedId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    @Id
    @BigIntegerGeneratedId // ✅ Correct for Hibernate 6.6
    @Column(name = "EMPLOYEE_ID", nullable = false, updatable = false)
    private BigInteger employeeId;

    @Column(name = "FIRST_NAME", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 100)
    private String lastName;

    @Column(name = "AGE", nullable = false)
    private Integer age;
}
