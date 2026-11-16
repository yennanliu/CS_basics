/*

1527. Patients With a Condition
https://leetcode.com/problems/patients-with-a-condition/
Easy

Table: Patients

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| patient_id   | int     |
| patient_name | varchar |
| conditions   | varchar |
+--------------+---------+
patient_id is the primary key (column with unique values) for this table.
'conditions' contains 0 or more code separated by spaces.
This table contains information of the patients in the hospital.

Write a solution to find the patient_id, patient_name, and conditions of the patients who have Type I Diabetes. Type I Diabetes always starts with DIAB1 prefix.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Patients table:
+------------+--------------+--------------+
| patient_id | patient_name | conditions   |
+------------+--------------+--------------+
| 1          | Daniel       | YFEV COUGH   |
| 2          | Alice        |              |
| 3          | Bob          | DIAB100 MYOP |
| 4          | George       | ACNE DIAB100 |
| 5          | Alain        | DIAB201      |
+------------+--------------+--------------+
Output:
+------------+--------------+--------------+
| patient_id | patient_name | conditions   |
+------------+--------------+--------------+
| 3          | Bob          | DIAB100 MYOP |
| 4          | George       | ACNE DIAB100 |
+------------+--------------+--------------+
Explanation: Bob and George both have a condition that starts with DIAB1.

*/

# V0
# IDEA : LIKE ON A WORD BOUNDARY -- AT THE START, OR AFTER A SPACE
#
#   conditions is a space-separated list of codes, and a code must START with
#   DIAB1. So DIAB1 is either the first code ('DIAB1%') or follows a space
#   ('% DIAB1%'). A bare '%DIAB1%' is wrong: it also matches 'SADIAB100',
#   where DIAB1 sits in the middle of another code.
#   A NULL conditions fails both LIKEs (NULL LIKE x is NULL, and WHERE keeps
#   only TRUE rows), and so does '' -- right either way: no codes, no diabetes.
#
# time = O(n * L), space = O(n)    -- n = rows in Patients, L = length of conditions; one scan
SELECT
    patient_id,
    patient_name,
    conditions
FROM Patients
WHERE conditions LIKE 'DIAB1%'       -- the first code
   OR conditions LIKE '% DIAB1%';    -- any later code, right after a space

# V1
# IDEA : REGEXP WITH AN ANCHOR -- START OF STRING OR A SPACE, THEN DIAB1
#
#   The same two cases as V0 in one pattern: (^| ) means "at the start, or
#   after a space". REGEXP is case-insensitive under MySQL's default
#   collation; that is harmless here, since condition codes are uppercase.
#
# time = O(n * L), space = O(n)    -- n = rows in Patients, L = length of conditions; one scan
SELECT
    patient_id,
    patient_name,
    conditions
FROM Patients
WHERE conditions REGEXP '(^| )DIAB1';
