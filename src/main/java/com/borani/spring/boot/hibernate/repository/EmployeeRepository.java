package com.borani.spring.boot.hibernate.repository;

import com.borani.spring.boot.hibernate.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface EmployeeRepository extends JpaRepository<Employee, BigInteger> {
}
