/*

Table Variables:

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| name          | varchar |
| value         | int     |
+---------------+---------+
name is the primary key for this table.
This table contains the stored variables and their values.
Table Expressions:

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| left_operand  | varchar |
| operator      | enum    |
| right_operand | varchar |
+---------------+---------+
(left_operand, operator, right_operand) is the primary key for this table.
This table contains a boolean expression that should be evaluated.
operator is an enum that takes one of the values ('<', '>', '=')
The values of left_operand and right_operand are guaranteed to be in the Variables table.
Write an SQL query to evaluate the boolean expressions in Expressions table.

Return the result table in any order.

The query result format is in the following example.

Variables table:
+------+-------+
| name | value |
+------+-------+
| x    | 66    |
| y    | 77    |
+------+-------+

Expressions table:
+--------------+----------+---------------+
| left_operand | operator | right_operand |
+--------------+----------+---------------+
| x            | >        | y             |
| x            | <        | y             |
| x            | =        | y             |
| y            | >        | x             |
| y            | <        | x             |
| x            | =        | x             |
+--------------+----------+---------------+

Result table:
+--------------+----------+---------------+-------+
| left_operand | operator | right_operand | value |
+--------------+----------+---------------+-------+
| x            | >        | y             | false |
| x            | <        | y             | true  |
| x            | =        | y             | false |
| y            | >        | x             | true  |
| y            | <        | x             | false |
| x            | =        | x             | true  |
+--------------+----------+---------------+-------+
As shown, you need find the value of each boolean exprssion in the table using the variables table.

*/

# V0
SELECT left_operand, operator, right_operand,
       CASE WHEN operator = '>' AND v1.value > v2.value THEN 'true'
            WHEN operator = '<' AND v1.value < v2.value THEN 'true'
            WHEN operator = '=' AND v1.value = v2.value THEN 'true'
            ELSE 'false' END AS value
FROM expressions e
LEFT JOIN variables v1
ON e.left_operand = v1.name
LEFT JOIN variables v2
ON e.right_operand = v2.name;

# V1
# https://code.dennyzhang.com/evaluate-boolean-expression
select t.left_operand, t.operator, t.right_operand, 
    (case when v1_value>v2.value and operator = '>' then "true"
          when v1_value<v2.value and operator = '<' then "true"
          when v1_value=v2.value and operator = '=' then "true"
          else "false"
          end) as value
from 
   (select e.*, v1.value as v1_value
    from Expressions as e inner join Variables as v1
    on e.left_operand = v1.name) as t inner join Variables as v2 
    on t.right_operand = v2.name

# V2
# Time:  O(n)
# Space: O(n)
SELECT left_operand, operator, right_operand,
       CASE WHEN operator = '>' AND v1.value > v2.value THEN 'true'
            WHEN operator = '<' AND v1.value < v2.value THEN 'true'
            WHEN operator = '=' AND v1.value = v2.value THEN 'true'
            ELSE 'false' END AS value
FROM expressions e
LEFT JOIN variables v1
ON e.left_operand = v1.name
LEFT JOIN variables v2
ON e.right_operand = v2.name;