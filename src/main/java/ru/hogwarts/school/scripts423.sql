SELECT s.name,s.age
FROM student s
LEFT JOIN faculty f on s.id=f.id;

SELECT s.name,s.age, a.id
FROM student s
         INNER JOIN avatar a on s.id=a.id;