package com.gschool.service;

import com.gschool.entities.Student;
import com.gschool.repositries.FiliereRepository;
import com.gschool.repositries.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public  final FiliereService filiereService;

    public StudentService(StudentRepository studentRepository , FiliereService filiereService) {
        this.studentRepository = studentRepository;
        this.filiereService = filiereService;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student addStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        if (savedStudent.getFiliere() != null) {
            filiereService.incrementNbrEtudiant(savedStudent.getFiliere().getId());
        }
        return savedStudent;
    }


    public Student updateStudent(Integer id, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            // Check if filiere is changing
            Integer oldFiliereId = (student.getFiliere() != null) ? student.getFiliere().getId() : null;
            Integer newFiliereId = (studentDetails.getFiliere() != null) ? studentDetails.getFiliere().getId() : null;

            // Update student fields
            student.setNom(studentDetails.getNom());
            student.setPrenom(studentDetails.getPrenom());
            student.setCne(studentDetails.getCne());
            student.setPhoto(studentDetails.getPhoto());
            student.setFiliere(studentDetails.getFiliere());

            Student updatedStudent = studentRepository.save(student);

            // Adjust filiere student count if changed
            if (oldFiliereId != null && !oldFiliereId.equals(newFiliereId)) {
                filiereService.decrementNbrEtudiant(oldFiliereId);
            }
            if (newFiliereId != null && !newFiliereId.equals(oldFiliereId)) {
                filiereService.incrementNbrEtudiant(newFiliereId);
            }

            return updatedStudent;
        }
        return null;
    }


    public void deleteStudent(Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Integer filiereId = (student.getFiliere() != null) ? student.getFiliere().getId() : null;
            studentRepository.deleteById(id);
            if (filiereId != null) {
                filiereService.decrementNbrEtudiant(filiereId);
            }
        }
    }
    public  long getTotalStudents() {
        return studentRepository.count();
    }
    public boolean existsByCne(String cne) {

        return studentRepository.existsByCne(cne);
    }
}
