package ru.hogwarts.school.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.services.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
@Tag(name = "Студенты.", description = "CRUD-операции и другие эндпоинты для работы со студентами.")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/total-number")
    @Operation(summary = "Получить количество всех студентов в школе.")
    public Integer getTotalNumberOfStudents() {
        return studentService.getTotalNumber();
    }

    @GetMapping("/avg-age")
    @Operation(summary = "Получить средний возраст студентов.")
    public Double getAverageAgeOfStudents() {
        return studentService.getAverageAge();
    }

    @GetMapping("last-five")
    @Operation(summary = "Получить только пять последних студентов. Последние студенты считаются теми, у кого идентификатор больше других.")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFive();
    }

    @GetMapping("{studentId}")
    @Operation(summary = "Получение студента по id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Студент получен."
            )
    })
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        Student foundStudent = studentService.getStudent(studentId);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @GetMapping("/age/{studentAge}")
    @Operation(summary = "Получение студентов по возрасту.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все студенты по возрасту получены."
            )
    })
    public ResponseEntity<Collection<Student>> getStudentsByAge(@PathVariable Integer studentAge) {
        Collection<Student> result = studentService.getStudentsByAge(studentAge);
        if (result.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Добавление студента.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Студент добавлен"
            )
    })
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(addedStudent);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editedStudent = studentService.editStudent(student);
        if (editedStudent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editedStudent);
    }

    @DeleteMapping("{studentId}")
    @Operation(summary = "Удаление студента по id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Студент удален."
            )})
    public ResponseEntity<?> removeStudent(@PathVariable Long studentId) {
        studentService.removeStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/age-between")
    @Operation(summary = "Фильтр студентов по возрасту от-до.")
    public Collection<Student> findByAgeBetween(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        return studentService.getStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/faculty/{studentId}")
    @Operation(summary = "Получить факультет по id студента.")
    public ResponseEntity<?> getStudentsFaculty(@PathVariable Long studentId) {
        Faculty f = studentService.getFaculty(studentId);
        if (f == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(f);
    }

    @GetMapping("/starts-with/{letter}")
    public Collection<String> getByFirstLetter(@PathVariable("letter") String letter) {
        return studentService.getByFirstLetter(letter);
    }

    @GetMapping("/avg-age-2")
    public Double getAverageAge() {
        return studentService.getAverageAgeAgain();
    }
}

