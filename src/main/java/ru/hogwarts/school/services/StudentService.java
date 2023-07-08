package ru.hogwarts.school.services;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositorys.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void removeStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.findByAgeLessThan(age);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findStudentsByAgeBetween(minAge, maxAge);
    }

    public Faculty getFaculty(long id) {
        Student s = studentRepository.findById(id).orElse(null);
        if (s == null) {
            return null;
        }
        return s.getFaculty();
    }

    public Integer getTotalNumber() {
        return studentRepository.getTotalNumber();
    }

    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getLastFive() {
        return studentRepository.getLastFive();
    }
}

