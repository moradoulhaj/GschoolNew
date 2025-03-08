package com.gschool.controller.web;

import com.gschool.entities.Student;
import com.gschool.entities.Filiere;
import com.gschool.service.StudentService;
import com.gschool.service.FiliereService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Value("${upload.directory}")
    private String uploadDirectory;
    @PersistenceContext
    private EntityManager entityManager;

    private final StudentService studentService;
    private final FiliereService filiereService;

    public StudentController(StudentService studentService, FiliereService filiereService) {
        this.studentService = studentService;
        this.filiereService = filiereService;
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
    public ResponseEntity<?> addStudent(
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("cne") String cne,
            @RequestParam("filiere.id") Integer filiereId,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {


        // Check if a student with the same CNE exists
        if (studentService.existsByCne(cne)) {
            System.out.println("Student with CNE " + cne + " already exists.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Student with the same CNE already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // Validate upload directory
        if (uploadDirectory == null || uploadDirectory.isEmpty()) {
            System.err.println("Upload directory is not configured.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Upload directory is not configured.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        try {
            // Fetch the Filiere object from the database
            Filiere filiere = filiereService.getFiliereById(filiereId);
            if (filiere == null) {
                System.out.println("Filière with ID " + filiereId + " not found.");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Filière not found.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Save the photo to the upload directory (if provided)
            String photoPath = null;
            if (photo != null && !photo.isEmpty()) {
                Path uploadPath = Paths.get(uploadDirectory);
                if (!Files.exists(uploadPath)) {
                    System.out.println("Creating upload directory: " + uploadPath);
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique file name
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                System.out.println("Saving photo to: " + filePath);
                Files.copy(photo.getInputStream(), filePath);

                photoPath = fileName;
            }

            // Create the student object
            Student student = new Student();
            student.setNom(nom);
            student.setPrenom(prenom);
            student.setCne(cne);
            student.setFiliere(filiere); // Set the fetched Filiere object
            student.setPhoto(photoPath);

            // Save the new Student
            System.out.println("Saving student: " + student);
            Student newStudent = studentService.addStudent(student);
            return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
        } catch (IOException e) {
            System.err.println("IOException while uploading photo: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to upload photo.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Integer id,
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("cne") String cne,
            @RequestParam("filiere.id") Integer filiereId,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        // Fetch the existing student by ID
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            return ResponseEntity.notFound().build(); // Student not found
        }

        // Detach the existingStudent object from the Hibernate session
        entityManager.detach(existingStudent);

        // Check if the CNE already exists for another student
        if (!existingStudent.getCne().equals(cne) && studentService.existsByCne(cne)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "CNE already exists.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            // Fetch the Filiere object from the database
            Filiere filiere = filiereService.getFiliereById(filiereId);
            if (filiere == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Filière not found.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Save the photo to the upload directory (if provided)
            String photoPath = existingStudent.getPhoto(); // Keep the existing photo by default
            if (photo != null && !photo.isEmpty()) {
                Path uploadPath = Paths.get(uploadDirectory);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique file name
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(photo.getInputStream(), filePath);

                photoPath = fileName;
            }

            // Update the student object
            existingStudent.setNom(nom);
            existingStudent.setPrenom(prenom);
            existingStudent.setCne(cne);
            existingStudent.setFiliere(filiere); // Set the fetched Filiere object
            existingStudent.setPhoto(photoPath);

            // Update the student
            Student updatedStudent = studentService.updateStudent(id, existingStudent);
            return ResponseEntity.ok(updatedStudent);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to upload photo.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudents() {
        long totalStudents = studentService.getTotalStudents();
        return ResponseEntity.ok(totalStudents);
    }
}