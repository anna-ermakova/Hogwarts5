package ru.hogwarts.school.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Faculty;

import java.util.Collection;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColorLike(String color);

    Collection<Faculty> findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(String color, String name);
}