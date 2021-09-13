/*

Description
https://leetcode.com/problems/convert-date-format/

SQL Schema

Table: Days

+-------------+------+
| Column Name | Type |
+-------------+------+
| day         | date |
+-------------+------+
day is the primary key for this table.
Write an SQL query to convert each date in Days into a string formatted as "day_name, month_name day, year".

Return the result table in any order.

The query result format is in the following example:

Days table:
+------------+
| day        |
+------------+
| 2022-04-12 |
| 2021-08-09 |
| 2020-06-26 |
+------------+

Result table:
+-------------------------+
| day                     |
+-------------------------+
| Tuesday, April 12, 2022 |
| Monday, August 9, 2021  |
| Friday, June 26, 2020   |
+-------------------------+
Please note that the output is case-sensitive.

*/

# V0
SELECT DATE_FORMAT(day, "%W, %M %e, %Y") AS day
FROM Days;

# V1
# https://goodtecher.com/leetcode-1853-convert-date-format/
SELECT DATE_FORMAT(`day`,'%W, %M %e, %Y') as day FROM Days

# V2
# Time:  O(n)
# Space: O(n)
SELECT DATE_FORMAT(day, "%W, %M %e, %Y") AS day
FROM Days;