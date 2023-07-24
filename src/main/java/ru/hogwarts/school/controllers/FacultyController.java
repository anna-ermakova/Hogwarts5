package ru.hogwarts.school.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.services.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@Tag(name = "Факультеты.", description = "CRUD-операции и другие эндпоинты для работы с факультетами.")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{facultyId}")
    @Operation(summary = "Получение факультета по id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Факультет получен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    }
            )
    })
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long facultyId) {
        Faculty foundFaculty = facultyService.getFaculty(facultyId);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @GetMapping("/color/{facultyColor}")
    @Operation(summary = "Получение факультета по цвету.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Факультет получен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    }
            )
    })
    @Parameters(value = {@Parameter(name = "color", example = "red")})
    public ResponseEntity<Collection<Faculty>> getFacultiesByColor(@PathVariable String facultyColor) {
        Collection<Faculty> result = facultyService.getFacultiesByColor(facultyColor);
        if (result.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Добавление факультета.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Факультет добавлен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    }
            )
    })
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        Faculty addedFaculty = facultyService.addFaculty(faculty);
        return ResponseEntity.ok(addedFaculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editedFaculty = facultyService.editFaculty(faculty);
        if (editedFaculty == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{facultyId}")
    @Operation(summary = "Удаление факультета по id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Факультет удален."
            )})
    public ResponseEntity<?> removeFaculty(@PathVariable Long facultyId) {
        facultyService.removeFaculty(facultyId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/color-or-name")
    @Operation(summary = "Фильтр факультетов по цвету или имени.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все факультеты по цвету или имени получены.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    }
            )
    })
    public Collection<Faculty> findByColorOrName(@RequestParam String param) {
        return facultyService.getFacultiesByColorOrName(param);
    }

    @GetMapping("/students/{facultyId}")
    @Operation(summary = "Получить студентов по id факультета.")
    public Collection<Student> getStudents(@PathVariable Long facultyId) {
        return facultyService.getStudents(facultyId);
    }

    @GetMapping("/longest-name")
    public String getLongestName() {
        return facultyService.getLongestName();
    }

    @GetMapping("/strange")
    public Integer sum() {
        return facultyService.getStrange();
    }
}
