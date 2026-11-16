/*

2356. Number of Unique Subjects Taught by Each Teacher
https://leetcode.com/problems/number-of-unique-subjects-taught-by-each-teacher/
Easy

Table: Teacher

+-------------+------+
| Column Name | Type |
+-------------+------+
| teacher_id  | int  |
| subject_id  | int  |
| dept_id     | int  |
+-------------+------+
(subject_id, dept_id) is the primary key (combinations of columns with unique values) of this table.
Each row in this table indicates that the teacher with teacher_id teaches the subject subject_id in the department dept_id.

Write a solution to calculate the number of unique subjects each teacher teaches in the university.

Return the result table in any order.

The result format is shown in the following example.

Example 1:

Input:
Teacher table:
+------------+------------+---------+
| teacher_id | subject_id | dept_id |
+------------+------------+---------+
| 1          | 2          | 3       |
| 1          | 2          | 4       |
| 1          | 3          | 3       |
| 2          | 1          | 1       |
| 2          | 2          | 1       |
| 2          | 3          | 1       |
| 2          | 4          | 1       |
+------------+------------+---------+
Output:
+------------+-----+
| teacher_id | cnt |
+------------+-----+
| 1          | 2   |
| 2          | 4   |
+------------+-----+
Explanation:
Teacher 1:
  - They teach subject 2 in departments 3 and 4.
  - They teach subject 3 in department 3.
Teacher 2:
  - They teach subject 1 in department 1.
  - They teach subject 2 in department 1.
  - They teach subject 3 in department 1.
  - They teach subject 4 in department 1.

*/

# V0
# IDEA : GROUP BY TEACHER, COUNT DISTINCT SUBJECTS
#
#   A teacher can teach the same subject in several departments (teacher 1
#   teaches subject 2 in depts 3 and 4), so one subject can sit on many rows.
#   COUNT(DISTINCT subject_id) counts each subject once per teacher;
#   COUNT(*) would count the rows and over-count.
#
# time = O(n), space = O(n)    -- n = rows in Teacher; one pass, a hash of (teacher, subject) pairs
SELECT
    teacher_id,
    COUNT(DISTINCT subject_id) AS cnt   -- DISTINCT: the same subject in two depts counts once
FROM Teacher
GROUP BY teacher_id;
