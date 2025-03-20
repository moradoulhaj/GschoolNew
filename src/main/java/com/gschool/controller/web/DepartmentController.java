package com.gschool.controller.web;

import com.gschool.entities.Department;
import com.gschool.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/api/dep")
public class DepartmentController {
    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity <Department> getDepartmentById(@PathVariable Integer id) {
        Department department = departmentService.getDepartmentById(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity <?> createDepartment(@RequestBody Department department) {
        //Check if the departement with the same name exists
        if (departmentService.existsByName(department.getName())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Entity with the same name already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        }

        Department newDep = departmentService.addDepartment(department);
        return new ResponseEntity<>(newDep, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity <?> updateDepartment(@PathVariable Integer id,@RequestBody Department department) {
        Department optionalDepartment = departmentService.getDepartmentById(id);
        if (optionalDepartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } else if (departmentService.existsByNameAndIdNot(department.getName() , id)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Entity with the same name already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        // updating the dep
        optionalDepartment.setName(department.getName());
        optionalDepartment.setTimedrops(department.getTimedrops());
        Department newDep = departmentService.updateDepartment(id , optionalDepartment);
        return ResponseEntity.ok(newDep);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable Integer id) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found.");
        }

        departmentService.deleteDepartment(id);
    }
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }




}
