/*

Table: Students

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| student_id    | int     |
| student_name  | varchar |
+---------------+---------+
student_id is the primary key for this table.
Each row of this table contains the ID and the name of one student in the school.
Table: Subjects

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| subject_name | varchar |
+--------------+---------+
subject_name is the primary key for this table.
Each row of this table contains a name of one subject in the school.
Table: Examinations

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| student_id   | int     |
| subject_name | varchar |
+--------------+---------+
There is no primary key for this table. It may contain duplicates.
Each student from Students table takes every course from Subjects table.
Each row of this table indicates that a student with ID student_id attended the exam of subject_name.
Write an SQL query to find the number of times each student attended each exam.

Order the result table by student_id and subject_name.

The query result format is in the following example:

Students table:
+------------+--------------+
| student_id | student_name |
+------------+--------------+
| 1          | Alice        |
| 2          | Bob          |
| 13         | John         |
| 6          | Alex         |
+------------+--------------+
Subjects table:
+--------------+
| subject_name |
+--------------+
| Math         |
| Physics      |
| Programming  |
+--------------+
Examinations table:
+------------+--------------+
| student_id | subject_name |
+------------+--------------+
| 1          | Math         |
| 1          | Physics      |
| 1          | Programming  |
| 2          | Programming  |
| 1          | Physics      |
| 1          | Math         |
| 13         | Math         |
| 13         | Programming  |
| 13         | Physics      |
| 2          | Math         |
| 1          | Math         |
+------------+--------------+
Result table:
+------------+--------------+--------------+----------------+
| student_id | student_name | subject_name | attended_exams |
+------------+--------------+--------------+----------------+
| 1          | Alice        | Math         | 3              |
| 1          | Alice        | Physics      | 2              |
| 1          | Alice        | Programming  | 1              |
| 2          | Bob          | Math         | 1              |
| 2          | Bob          | Physics      | 0              |
| 2          | Bob          | Programming  | 1              |
| 6          | Alex         | Math         | 0              |
| 6          | Alex         | Physics      | 0              |
| 6          | Alex         | Programming  | 0              |
| 13         | John         | Math         | 1              |
| 13         | John         | Physics      | 1              |
| 13         | John         | Programming  | 1              |
+------------+--------------+--------------+----------------+
The result table should contain all students and all subjects.
Alice attended Math exam 3 times, Physics exam 2 times and Programming exam 1 time.
Bob attended Math exam 1 time, Programming exam 1 time and didn't attend the Physics exam.
Alex didn't attend any exam.
John attended Math exam 1 time, Physics exam 1 time and Programming exam 1 time.

*/

# V0
### NOTE :  left join
#           and NULLIF(COUNT(e.subject_name), 0)
SELECT
s1.student_id,
s1.student_name,
e.subject_name
NULLIF(COUNT(e.subject_name), 0) as attended_exams
FROM
Student s1
LEFT JOIN
Examinations e
ON
s1.student_id = e.student_id
GROUP BY
s1.student_id,
s1.student_name,
e.subject_name

# V1
# https://code.dennyzhang.com/students-and-examinations
select Students.student_id, 
student_name, 
Subjects.subject_name, 
count(Examinations.student_id) as attended_exams
from Students join Subjects
left join Examinations
on Students.student_id = Examinations.student_id 
and 
Subjects.subject_name = Examinations.subject_name
group by Students.student_id, subject_name

# V1'
# https://code.dennyzhang.com/students-and-examinations
select Students.student_id, student_name, Subjects.subject_name, sum(if(isnull(Examinations.student_id), 0, 1)) as attended_exams
from Students join Subjects
left join Examinations
on Students.student_id = Examinations.student_id and Subjects.subject_name = Examinations.subject_name
group by Students.student_id, subject_name

# V2
# Time:  O((m * n) * log(m * n))
# Space: O(m * n)
SELECT a.student_id, 
       a.student_name, 
       b.subject_name, 
       Count(c.subject_name) AS attended_exams 
FROM   students AS a 
       CROSS JOIN subjects AS b 
       LEFT JOIN examinations AS c 
              ON a.student_id = c.student_id 
                 AND b.subject_name = c.subject_name 
GROUP  BY a.student_id, 
          b.subject_name
ORDER  BY a.student_id, 
          b.subject_name;