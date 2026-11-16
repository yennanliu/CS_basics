/*

596. Classes With at Least 5 Students
https://leetcode.com/problems/classes-with-at-least-5-students/
Easy

Table: Courses

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| student     | varchar |
| class       | varchar |
+-------------+---------+
(student, class) is the primary key (combination of columns with unique values) for this table.
Each row of this table indicates the name of a student and the class in which they are enrolled.

Write a solution to find all the classes that have at least five students.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Courses table:
+---------+----------+
| student | class    |
+---------+----------+
| A       | Math     |
| B       | English  |
| C       | Math     |
| D       | Biology  |
| E       | Math     |
| F       | Computer |
| G       | Math     |
| H       | Math     |
| I       | Math     |
+---------+----------+
Output:
+---------+
| class   |
+---------+
| Math    |
+---------+
Explanation:
- Math has 6 students, so we include it.
- English has 1 student, so we do not include it.
- Biology has 1 student, so we do not include it.
- Computer has 1 student, so we do not include it.

*/

# V0
# IDEA : GROUP BY CLASS, FILTER THE GROUPS WITH HAVING
#
#   WHERE runs before grouping and cannot see a count; HAVING runs after, on
#   one row per class. (student, class) is the primary key, so a student is
#   never listed twice in one class and COUNT(student) needs no DISTINCT.
#
# time = O(n), space = O(k)    -- n = rows in Courses, k = distinct classes; one pass into a hash per class
SELECT class
FROM Courses
GROUP BY class
HAVING COUNT(student) >= 5;
