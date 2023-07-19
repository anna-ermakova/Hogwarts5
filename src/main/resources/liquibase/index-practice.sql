--liquibase formatted sql

--changeset anna-ermakova:1
--rollback DROP INDEX IDX_student_name
CREATE INDEX IDX_student_name ON student (name);

--changeset anna-ermakova:2
--rollback DROP INDEX IDX_faculty_name_color
CREATE INDEX IDX_faculty_name_color ON faculty (name, color);
