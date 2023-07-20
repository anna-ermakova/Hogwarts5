package ru.hogwarts.school.services;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositorys.FacultyRepository;
import ru.hogwarts.school.repositorys.StudentRepository;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        LOG.info("Was invoked method create");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        LOG.info("Was invoked method getFaculty");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        LOG.info("Was invoked method editFaculty");
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(long id) {
        LOG.info("Was invoked method removeFaculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        LOG.info("Was invoked method getFacultiesByColor");
        return facultyRepository.findByColorLike(color);
    }

    public Collection<Faculty> getAll() {
        LOG.info("Was invoked method getAll");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColorOrName(String param) {
        LOG.info("Was invoked method getFacultiesByColorOrName");
        return facultyRepository.findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(param, param);
    }

    public Collection<Student> getStudents(Long facultyId) {
        LOG.info("Was invoked method getStudents");
        return studentRepository.findAll().stream()
                .filter(s -> s.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
}
