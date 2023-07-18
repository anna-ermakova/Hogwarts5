ALTER TABLE student
    ADD CONSTRAINT age_more_then_15 CHECK (age > 15);
ALTER TABLE student
    ADD CONSTRAINT name_unique UNIQUE (name);
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;
ALTER TABLE student
    ALTER COLUMN name SET DEFAULT 20;

ALTER TABLE faculty
    ADD CONSTRAINT name_color_unique UNIQUE (name, color);
