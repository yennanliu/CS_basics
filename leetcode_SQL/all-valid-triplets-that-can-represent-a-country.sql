/*

1623. All Valid Triplets That Can Represent a Country
SQL Schema 
Table: SchoolA

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| student_id    | int     |
| student_name  | varchar |
+---------------+---------+
student_id is the primary key for this table.
Each row of this table contains the name and the id of a student in school A.
All student_name are distinct.
 

Table: SchoolB

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| student_id    | int     |
| student_name  | varchar |
+---------------+---------+
student_id is the primary key for this table.
Each row of this table contains the name and the id of a student in school B.
All student_name are distinct.
 

Table: SchoolC

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| student_id    | int     |
| student_name  | varchar |
+---------------+---------+
student_id is the primary key for this table.
Each row of this table contains the name and the id of a student in school C.
All student_name are distinct.
 

There is a country with three schools, where each student is enrolled in exactly one school. The country is joining a competition and wants to select one student from each school to represent the country such that:

member_A is selected from SchoolA,
member_B is selected from SchoolB,
member_C is selected from SchoolC, and
The selected students' names and IDs are pairwise distinct (i.e. no two students share the same name, and no two students share the same ID).
Write an SQL query to find all the possible triplets representing the country under the given constraints.

Return the result table in any order.

The query result format is in the following example.

 

SchoolA table:
+------------+--------------+
| student_id | student_name |
+------------+--------------+
| 1          | Alice        |
| 2          | Bob          |
+------------+--------------+

SchoolB table:
+------------+--------------+
| student_id | student_name |
+------------+--------------+
| 3          | Tom          |
+------------+--------------+

SchoolC table:
+------------+--------------+
| student_id | student_name |
+------------+--------------+
| 3          | Tom          |
| 2          | Jerry        |
| 10         | Alice        |
+------------+--------------+

Result table:
+----------+----------+----------+
| member_A | member_B | member_C |
+----------+----------+----------+
| Alice    | Tom      | Jerry    |
| Bob      | Tom      | Alice    |
+----------+----------+----------+
Let us see all the possible triplets.
- (Alice, Tom, Tom) --> Rejected because member_B and member_C have the same name and the same ID.
- (Alice, Tom, Jerry) --> Valid triplet.
- (Alice, Tom, Alice) --> Rejected because member_A and member_C have the same name.
- (Bob, Tom, Tom) --> Rejected because member_B and member_C have the same name and the same ID.
- (Bob, Tom, Jerry) --> Rejected because member_A and member_C have the same ID.
- (Bob, Tom, Alice) --> Valid triplet.

*/

# V0

# V1
# https://zhuanlan.zhihu.com/p/267293346
SELECT a.student_name AS 'member_A',
b.student_name AS 'member_B',
c.student_name AS 'member_C'
FROM SchoolA AS a
JOIN SchoolB AS b
ON a.student_id <> b.student_id
AND a.student_name <> b.student_name
JOIN SchoolC AS c
ON a.student_id <> c.student_id
AND b.student_id <> c.student_id
AND a.student_name <> c.student_name
AND b.student_name <> c.student_name;

# V1'
# https://leetcode.ca/all/1623.html
# https://leetcode.ca/2020-05-10-1623-All-Valid-Triplets-That-Can-Represent-a-Country/
select
    a.student_name member_A,
    b.student_name member_B,
    c.student_name member_C
from SchoolA a, SchoolB b, SchoolC c
where a.student_name != b.student_name
and a.student_name != c.student_name
and b.student_name != c.student_name
and a.student_id != b.student_id
and a.student_id != c.student_id
and b.student_id != c.student_id

# V2
# Time:  O(n^3)
# Space: O(n^3)
SELECT a.student_name AS member_A,
       b.student_name AS member_B,
       c.student_name AS member_c
FROM schoola a
INNER JOIN schoolb b ON (a.student_id != b.student_id
                         AND a.student_name != b.student_name)
INNER JOIN schoolc c ON (a.student_id != c.student_id
                         AND a.student_name != c.student_name)
AND (b.student_id != c.student_id
     AND b.student_name != c.student_name);