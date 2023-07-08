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
import ru.hogwarts.school.repositorys.StudentRepository;
import ru.hogwarts.school.services.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    MockMvc mvc;

    @MockBean
    StudentRepository studentRepository;

    @SpyBean
    StudentService studentService;

    @Test
    void getStudentPositiveTest() throws Exception {
        long id = 1;
        String name = "A";
        int age = 11;
        Student s1 = new Student(id, name, age);

        Mockito.when(studentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(s1));

        mvc.perform(MockMvcRequestBuilders.get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));

        Mockito.verify(studentRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getStudentNegativeTest() throws Exception {
        Mockito.when(studentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/student/42")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getStudentByAgePositiveTest() throws Exception {
        Student s1 = new Student(1, "A", 10);
        Student s2 = new Student(2, "B", 10);

        List<Student> list = List.of(s1, s2);

        Mockito.when(studentRepository.findByAgeLessThan(ArgumentMatchers.anyInt())).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/student/age/10")
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

    @Test
    void getStudentByAgeNegativeTest() throws Exception {
        Mockito.when(studentRepository.findByAgeLessThan(ArgumentMatchers.anyInt())).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/student/age/100500")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addStudentTest() throws Exception {
        long id = 1;
        String name = "A";
        int age = 11;
        Student s = new Student(id, name, age);

        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("age", age);

        Mockito.when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(s);

        mvc.perform(MockMvcRequestBuilders.post("/student")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jo.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));

    }

    @Test
    void editStudentPositiveTest() throws Exception {
        long id = 1;
        String name = "A";
        int age = 11;
        Student s = new Student(id, name, age);

        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("age", age);

        Mockito.when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(s);

        mvc.perform(MockMvcRequestBuilders.put("/student")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jo.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));

    }

    @Test
    void editStudentNegativeTest() throws Exception {
        Mockito.when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void removeStudentTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findStudentsByAgeBetweenTest() throws Exception {
        Student s1 = new Student(1, "A", 10);
        Student s2 = new Student(2, "B", 20);

        List<Student> list = List.of(s1, s2);

        Mockito.when(studentRepository.findStudentsByAgeBetween(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/student/age-between")
                        .param("minAge", "10")
                        .param("maxAge", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(s1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(s1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].age").value(s1.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(s2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(s2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].age").value(s2.getAge()));
    }

    @Test
    void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1, "Faculty", "green");
        Student student = new Student(1, "Student", 15);
        student.setFaculty(faculty);

        Mockito.when(studentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(student));

        mvc.perform(MockMvcRequestBuilders.get("/student/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(faculty.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(faculty.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(faculty.getColor()));
    }
}
