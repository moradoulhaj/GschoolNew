package com.gschool.service;

import com.gschool.entities.Student;
import com.gschool.repositries.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Integer id, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setNom(studentDetails.getNom());
            student.setPrenom(studentDetails.getPrenom());
            student.setCne(studentDetails.getCne());
            student.setFiliere(studentDetails.getFiliere());
            student.setPhoto(studentDetails.getPhoto());
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }
}
