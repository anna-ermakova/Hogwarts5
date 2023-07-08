package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositorys.StudentRepository;
import ru.hogwarts.school.services.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StudentServiceTests {

    @Mock
    private StudentRepository repositoryMock;

    @InjectMocks
    StudentService out;

    @Test
    void addStudentTest() {
        Student s = new Student(1, "AAAA", 17, null);
        Mockito.when(repositoryMock.save(s)).thenReturn(s);
        Assertions.assertEquals(s, out.addStudent(s));
    }

    @Test
    void getStudentPositiveTest() {
        Student s = new Student(1L, "AAAA", 17, null);
        Mockito.when(repositoryMock.findById(1L)).thenReturn(Optional.of(s));
        Assertions.assertEquals(s, out.getStudent(1));
    }


    @Test
    void getStudentNegativeTest() {
        when(repositoryMock.findById(4L)).thenReturn(Optional.empty());
        Assertions.assertNull(out.getStudent(4));
    }


    @Test
    void editStudentTest() {
        Student s = new Student(1, "CCC", 35, null);
        Mockito.when(repositoryMock.save(s)).thenReturn(s);
        Assertions.assertEquals(s, out.editStudent(s));
    }

    @Test
    void getStudentsByAgePositiveTest() {
        Student s = new Student(4, "DDD", 30, null);
        Mockito.when(repositoryMock.findByAgeLessThan(31)).thenReturn(List.of(s));
        Assertions.assertIterableEquals(List.of(s), out.getStudentsByAge(31));
    }

    @Test
    void getStudentsByAgeNegativeTest() {
        List<Student> test = Collections.emptyList();
        Mockito.when(repositoryMock.findByAgeLessThan(31)).thenReturn(test);
        Assertions.assertIterableEquals(test, out.getStudentsByAge(31));
    }
}
