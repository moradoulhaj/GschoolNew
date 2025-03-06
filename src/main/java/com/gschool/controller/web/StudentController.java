package com.gschool.controller.web;

import com.gschool.entities.Student;
import com.gschool.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@Valid @RequestBody Student student) {
        // Check if a student with the same CNE exists
        if (studentService.existsByCne(student.getCne())) {
            // Return conflict status with an error message in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Student with the same CNE already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // Save the new Student if no duplicate is found
        Student newStudent = studentService.addStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id, @Valid @RequestBody Student studentDetails) {
        // Fetch the existing student by ID
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            return ResponseEntity.notFound().build(); // Student not found
        }

        // Check if the CNE already exists for another student
        if (!existingStudent.getCne().equals(studentDetails.getCne()) &&
                studentService.existsByCne(studentDetails.getCne())) {
            // Return an error message wrapped in ResponseEntity
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "CNE already exists.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Update the student
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    // Endpoint to get the total count of students
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudents() {
        long totalStudents = studentService.getTotalStudents();
        return ResponseEntity.ok(totalStudents);
    }}

