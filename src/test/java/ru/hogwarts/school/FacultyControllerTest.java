package ru.hogwarts.school;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repositorys.FacultyRepository;
import ru.hogwarts.school.repositorys.StudentRepository;
import ru.hogwarts.school.services.FacultyService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacultyControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    MockMvc mvc;

    @MockBean
    FacultyRepository facultyRepository;

    @MockBean
    StudentRepository studentRepository;

    @SpyBean
    FacultyService facultyService;

    @Test
    void getFacultyPositiveTest() throws Exception {
        long id = 1;
        String name = "Faculty";
        String color = "green";
        Faculty faculty = new Faculty(id, name, color);

        Mockito.when(facultyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(faculty));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(color));

        Mockito.verify(facultyRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getFacultyNegativeTest() throws Exception {
        Mockito.when(facultyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/faculty/42")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getFacultyByColorPositiveTest() throws Exception {
        Faculty f1 = new Faculty(1, "A", "green");
        Faculty f2 = new Faculty(2, "B", "green");

        List<Faculty> list = List.of(f1, f2);

        Mockito.when(facultyRepository.findByColorLike(ArgumentMatchers.anyString())).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/faculty/color/green")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(f1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(f1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].color").value(f1.getColor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(f2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(f2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].color").value(f2.getColor()));

    }

    @Test
    void getFacultyByColorNegativeTest() throws Exception {
        Mockito.when(facultyRepository.findByColorLike(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/faculty/color/blue")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addFacultyTest() throws Exception {
        long id = 1;
        String name = "A";
        String color = "green";
        Faculty faculty = new Faculty(id, name, color);

        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("color", color);

        Mockito.when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);

        mvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jo.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(color));

    }

    @Test
    void editFacultyPositiveTest() throws Exception {
        long id = 1;
        String name = "A";
        String color = "green";
        Faculty faculty = new Faculty(id, name, color);

        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("color", color);

        Mockito.when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);

        mvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jo.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(color));

    }

    @Test
    void editFacultyNegativeTest() throws Exception {
        Mockito.when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void removeFacultyTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findFacultyByColorOrNameTest() throws Exception {
        Faculty f1 = new Faculty(1, "A", "green");
        Faculty f2 = new Faculty(2, "B", "green");

        List<Faculty> list = List.of(f1, f2);

        Mockito.when(facultyRepository.findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/faculty/color-or-name?param=green")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(f1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(f1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].color").value(f1.getColor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(f2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(f2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].color").value(f2.getColor()));
    }

    @Test
    void getStudentsTest() throws Exception {
        Faculty f1 = new Faculty(1, "Faculty1", "green");
        Faculty f2 = new Faculty(2, "Faculty2", "red");
        Student s1 = new Student(1, "A", 10, f1);
        Student s2 = new Student(2, "B", 20, f1);
        Student s3 = new Student(3, "B", 20, f2);

        List<Student> list = List.of(s1, s2, s3);

        Mockito.when(studentRepository.findAll()).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/faculty/students/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(s1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(s1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].age").value(s1.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(s2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(s2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].age").value(s2.getAge()));
    }
}

