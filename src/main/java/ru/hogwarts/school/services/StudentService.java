package ru.hogwarts.school.services;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositorys.StudentRepository;

import java.util.Collection;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

@Service
public class StudentService {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        LOG.info("Was invoked method create");
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        LOG.info("Was invoked method get");
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        LOG.info("Was invoked method update");
        return studentRepository.save(student);
    }

    public void removeStudent(long id) {
        LOG.info("Was invoked method delete");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getStudentsByAge(int age) {
        LOG.info("Was invoked method getStudentsByAge");
        return studentRepository.findByAgeLessThan(age);
    }

    public Collection<Student> getAll() {
        LOG.info("Was invoked method getAll");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        LOG.info("Was invoked method getStudentsByAgeBetween");
        return studentRepository.findStudentsByAgeBetween(minAge, maxAge);
    }

    public Faculty getFaculty(long id) {
        LOG.info("Was invoked method getFaculty");
        Student s = studentRepository.findById(id).orElse(null);
        if (s == null) {
            return null;
        }
        return s.getFaculty();
    }

    public Integer getTotalNumber() {
        LOG.info("Was invoked method getTotalNumber");
        return studentRepository.getTotalNumber();
    }

    public Double getAverageAge() {
        LOG.info("Was invoked method getAverageAge");
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getLastFive() {
        LOG.info("Was invoked method getLastFive");
        return studentRepository.getLastFive();
    }

    public Collection<String> getByFirstLetter(String letter) {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.substring(0, 1).equalsIgnoreCase(letter))
                .sorted()
                .map(String::toUpperCase)
                .collect(toList());
    }

    public Double getAverageAgeAgain() {
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0.0);
    }
}

