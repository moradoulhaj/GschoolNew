package com.gschool.service;


import com.gschool.entities.Department;
import com.gschool.repositries.DepartmentRepository;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public Department updateDepartment(Integer id , Department department) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department updatedDepartment = optionalDepartment.get();
            updatedDepartment.setName(department.getName());
            updatedDepartment.setTimedrops(department.getTimedrops());
            return departmentRepository.save(updatedDepartment);
        }
        return null;
    }
    public void deleteDepartment(Integer id) {
         departmentRepository.deleteById(id);
    }
    public boolean existsByNameAndIdNot(String name , Integer id) {
        return departmentRepository.existsByNameAndIdNot(name,id);
    }
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

}
