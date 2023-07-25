CREATE TABLE cars
(
    id    BIGSERIAL PRIMARY KEY,
    brand VARCHAR(15)             NOT NULL,
    model VARCHAR(31)             NOT NULL,
    price INT CHECK ( price > 0 ) NOT NULL
);
CREATE TABLE owners
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(15)                 NOT NULL,
    age                INT CHECK ( age > 17)       NOT NULL,
    has_driver_license BOOLEAN DEFAULT true        NOT NULL,
    car_id             BIGINT REFERENCES cars (id) NOT NULL
);
INSERT INTO cars(brand, model, price)
VALUES ('lada', 'vesta', 1100000),
       ('Toyota', 'camry', 2100000);

INSERT INTO owners(name, age, car_id)
VALUES ('Bod', 23, 1),
       ('Nik', 20, 1),
       ('Oleg', 54, 2),
       ('Ivan', 36, 2);