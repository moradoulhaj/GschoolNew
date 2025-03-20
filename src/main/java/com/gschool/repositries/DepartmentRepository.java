package com.gschool.repositries;

import com.gschool.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByName(String name);
}