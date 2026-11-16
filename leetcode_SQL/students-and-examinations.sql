/*

1280. Students and Examinations
https://leetcode.com/problems/students-and-examinations/
Easy

Table: Students

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| student_id    | int     |
| student_name  | varchar |
+---------------+---------+
student_id is the primary key (column with unique values) for this table.
Each row of this table contains the ID and the name of one student in the school.

Table: Subjects

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| subject_name | varchar |
+--------------+---------+
subject_name is the primary key (column with unique values) for this table.
Each row of this table contains the name of one subject in the school.

Table: Examinations

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| student_id   | int     |
| subject_name | varchar |
+--------------+---------+
There is no primary key (column with unique values) for this table. It may contain duplicates.
Each student from the Students table takes every course from the Subjects table.
Each row of this table indicates that a student with ID student_id attended the exam of subject_name.

Write a solution to find the number of times each student attended each exam.

Return the result table ordered by student_id and subject_name.

The result format is in the following example.

Example 1:

Input:
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
Output:
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
Explanation:
The result table should contain all students and all subjects.
Alice attended the Math exam 3 times, the Physics exam 2 times, and the Programming exam 1 time.
Bob attended the Math exam 1 time, the Programming exam 1 time, and did not attend the Physics exam.
Alex did not attend any exams.
John attended the Math exam 1 time, the Physics exam 1 time, and the Programming exam 1 time.

*/

# V0
# IDEA : CROSS JOIN EVERY (STUDENT, SUBJECT) PAIR, LEFT JOIN THE ATTENDANCES, COUNT
#
#   The answer has a row for every student x every subject, even with 0 exams
#   attended, so the pairs come from a CROSS JOIN -- Examinations cannot supply
#   them, it only holds the attended ones.
#   LEFT JOIN Examinations on both student and subject: an unattended pair keeps
#   one row with e.* NULL. COUNT(e.subject_name) skips NULLs, so that pair
#   counts 0; COUNT(*) would wrongly count it as 1.
#
# time = O(s*j*log(s*j) + x), space = O(s*j + x)    -- s = students, j = subjects, x = exam rows; the ORDER BY sort sets the log
SELECT
    st.student_id,
    st.student_name,
    sub.subject_name,
    COUNT(e.subject_name) AS attended_exams   -- skips the NULL row of an unattended pair -> 0
FROM Students AS st
CROSS JOIN Subjects AS sub
LEFT JOIN Examinations AS e
    ON st.student_id = e.student_id
   AND sub.subject_name = e.subject_name
GROUP BY
    st.student_id,
    st.student_name,
    sub.subject_name
ORDER BY
    st.student_id,
    sub.subject_name;

# V1
# IDEA : PRE-AGGREGATE THE ATTENDANCES, THEN LEFT JOIN ONTO THE GRID
#
#   Count Examinations once per (student, subject) first, so the join meets one
#   row per pair instead of every duplicate attendance. A pair with no count
#   comes back NULL from the LEFT JOIN, and COALESCE turns it into 0.
#
# time = O(s*j*log(s*j) + x), space = O(s*j + x)    -- same bounds; the join is never fanned out by duplicates
WITH attendance AS (
    SELECT
        student_id,
        subject_name,
        COUNT(*) AS exam_count
    FROM Examinations
    GROUP BY
        student_id,
        subject_name
)
SELECT
    st.student_id,
    st.student_name,
    sub.subject_name,
    COALESCE(a.exam_count, 0) AS attended_exams   -- no attendance row -> NULL -> 0
FROM Students AS st
CROSS JOIN Subjects AS sub
LEFT JOIN attendance AS a
    ON st.student_id = a.student_id
   AND sub.subject_name = a.subject_name
ORDER BY
    st.student_id,
    sub.subject_name;
