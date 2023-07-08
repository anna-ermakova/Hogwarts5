package ru.hogwarts.school.services;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositorys.FacultyRepository;
import ru.hogwarts.school.repositorys.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findByColorLike(color);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColorOrName(String param) {
        return facultyRepository.findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(param, param);
    }

    public Collection<Student> getStudents(Long facultyId) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
}
