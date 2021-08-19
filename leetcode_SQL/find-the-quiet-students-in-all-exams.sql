/*

Table: Student

+---------------------+---------+
| Column Name         | Type    |
+---------------------+---------+
| student_id          | int     |
| student_name        | varchar |
+---------------------+---------+
student_id is the primary key for this table.
student_name is the name of the student.
Table: Exam

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| exam_id       | int     |
| student_id    | int     |
| score         | int     |
+---------------+---------+
(exam_id, student_id) is the primary key for this table.
Student with student_id got score points in exam with id exam_id.
A “quite” student is the one who took at least one exam and didn’t score neither the high score nor the low score.

Write an SQL query to report the students (student_id, student_name) being “quiet” in ALL exams.

Don’t return the student who has never taken any exam. Return the result table ordered by student_id.

The query result format is in the following example.

Student table:
+-------------+---------------+
| student_id  | student_name  |
+-------------+---------------+
| 1           | Daniel        |
| 2           | Jade          |
| 3           | Stella        |
| 4           | Jonathan      |
| 5           | Will          |
+-------------+---------------+

Exam table:
+------------+--------------+-----------+
| exam_id    | student_id   | score     |
+------------+--------------+-----------+
| 10         |     1        |    70     |
| 10         |     2        |    80     |
| 10         |     3        |    90     |
| 20         |     1        |    80     |
| 30         |     1        |    70     |
| 30         |     3        |    80     |
| 30         |     4        |    90     |
| 40         |     1        |    60     |
| 40         |     2        |    70     |
| 40         |     4        |    80     |
+------------+--------------+-----------+

Result table:
+-------------+---------------+
| student_id  | student_name  |
+-------------+---------------+
| 2           | Jade          |
+-------------+---------------+

For exam 1: Student 1 and 3 hold the lowest and high score respectively.
For exam 2: Student 1 hold both highest and lowest score.
For exam 3 and 4: Studnet 1 and 4 hold the lowest and high score respectively.
Student 2 and 5 have never got the highest or lowest in any of the exam.
Since student 5 is not taking any exam, he is excluded from the result.
So, we only return the information of Student 2.

*/


# V0
select distinct Student.*
from Student inner join Exam
on Student.student_id = Exam.student_id
where student.student_id not in 
    (select e1.student_id
    from Exam as e1 inner join
        (select exam_id, min(score) as min_score, max(score) as max_score
        from Exam
        group by exam_id) as e2
    on e1.exam_id = e2.exam_id
    where e1.score = e2.min_score or e1.score = e2.max_score)
order by student_id

# V1
# https://code.dennyzhang.com/find-the-quiet-students-in-all-exams
select distinct Student.*
from Student inner join Exam
on Student.student_id = Exam.student_id
where student.student_id not in 
    (select e1.student_id
    from Exam as e1 inner join
        (select exam_id, min(score) as min_score, max(score) as max_score
        from Exam
        group by exam_id) as e2
    on e1.exam_id = e2.exam_id
    where e1.score = e2.min_score or e1.score = e2.max_score)
order by student_id

# V2
# Time:  O(m + nlogn)
# Space: O(m + n)
SELECT s.student_id, 
       s.student_name 
FROM   student s 
       INNER JOIN (SELECT a.student_id, 
                          Count(a.exam_id) AS total_exam, 
                          Sum(CASE 
                                WHEN a.score > min_score 
                                     AND a.score < max_score THEN 1 
                                ELSE 0 
                              END)         AS quite_exam 
                   FROM   exam a 
                          INNER JOIN (SELECT exam_id, 
                                             Min(score) AS min_score, 
                                             Max(score) AS max_score 
                                      FROM   exam 
                                      GROUP  BY exam_id 
                                      ORDER  BY NULL) b 
                                  ON a.exam_id = b.exam_id 
                   GROUP  BY a.student_id 
                   ORDER  BY NULL) c 
               ON s.student_id = c.student_id 
WHERE  c.total_exam = c.quite_exam 
ORDER  BY s.student_id; 