package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repositorys.FacultyRepository;
import ru.hogwarts.school.services.FacultyService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTests {

    @Mock
    private FacultyRepository repositoryMock;

    @InjectMocks
    FacultyService out;

    @Test
    void addFacultyTest() {
        Faculty f = new Faculty(4, "DDD", "green");
        Mockito.when(repositoryMock.save(f)).thenReturn(f);
        assertEquals(f, out.addFaculty(f));
    }

    @Test
    void getFacultyPositiveTest() {
        Faculty f = new Faculty(4, "DDD", "green");
        Mockito.when(repositoryMock.findById(4L)).thenReturn(Optional.of(f));
        assertEquals(f, out.getFaculty(4));
    }

    @Test
    void getFacultyNegativeTest() {
        Mockito.when(repositoryMock.findById(4L)).thenReturn(Optional.empty());
        assertNull(out.getFaculty(4));
    }

    @Test
    void editFacultyTest() {
        Faculty f = new Faculty(3, "CCC", "black");
        Mockito.when(repositoryMock.save(f)).thenReturn(f);
        assertEquals(f, out.editFaculty(f));
    }

    @Test
    void getFacultiesByColorPositiveTest() {
        Faculty f = new Faculty(4, "DDD", "green");
        Mockito.when(repositoryMock.findByColorLike("green")).thenReturn(List.of(f));
        assertIterableEquals(List.of(f), out.getFacultiesByColor("green"));
    }

    @Test
    void getFacultiesByColorNegativeTest() {
        List<Faculty> test = Collections.emptyList();
        Mockito.when(repositoryMock.findByColorLike("black")).thenReturn(test);
        assertIterableEquals(test, out.getFacultiesByColor("black"));
    }
}
