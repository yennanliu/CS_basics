/*

1757. Recyclable and Low Fat Products
https://leetcode.com/problems/recyclable-and-low-fat-products/
Easy

Table: Products

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| product_id  | int     |
| low_fats    | enum    |
| recyclable  | enum    |
+-------------+---------+
product_id is the primary key (column with unique values) for this table.
low_fats is an ENUM (category) of type ('Y', 'N') where 'Y' means this product is low fat and 'N' means it is not.
recyclable is an ENUM (category) of types ('Y', 'N') where 'Y' means this product is recyclable and 'N' means it is not.

Write a solution to find the ids of products that are both low fat and recyclable.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Products table:
+-------------+----------+------------+
| product_id  | low_fats | recyclable |
+-------------+----------+------------+
| 0           | Y        | N          |
| 1           | Y        | Y          |
| 2           | N        | Y          |
| 3           | Y        | Y          |
| 4           | N        | N          |
+-------------+----------+------------+
Output:
+-------------+
| product_id  |
+-------------+
| 1           |
| 3           |
+-------------+
Explanation: Only products 1 and 3 are both low fat and recyclable.

*/

# V0
# IDEA : FILTER ON BOTH FLAGS
#
#   A product qualifies only when both ENUM columns are 'Y', so one WHERE with
#   AND does it. Both columns are ENUM('Y', 'N') and never NULL, so no NULL
#   branch is needed.
#
# time = O(n), space = O(n)    -- n = rows in Products; one scan, up to n rows returned
SELECT product_id
FROM Products
WHERE low_fats = 'Y'
  AND recyclable = 'Y';
