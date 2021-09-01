/*

Product's Worth Over Invoices Problem

LeetCode Problem 1677.

Table: Product

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| product_id  | int     |
| name        | varchar |
+-------------+---------+
product_id is the primary key for this table.
This table contains the ID and the name of the product. The name consists of only lowercase English letters. No two products have the same name.
Table: Invoice

+-------------+------+
| Column Name | Type |
+-------------+------+
| invoice_id  | int  |
| product_id  | int  |
| rest        | int  |
| paid        | int  |
| canceled    | int  |
| refunded    | int  |
+-------------+------+
invoice_id is the primary key for this table and the id of this invoice.
product_id is the id of the product for this invoice.
rest is the amount left to pay for this invoice.
paid is the amount paid for this invoice.
canceled is the amount canceled for this invoice.
refunded is the amount refunded for this invoice.
Write an SQL query that will, for all products, return each product name with total amount due, paid, canceled, and refunded across all invoices.

Return the result table ordered by product_name.

The query result format is in the following example:

Product table:
+------------+-------+
| product_id | name  |
+------------+-------+
| 0          | ham   |
| 1          | bacon |
+------------+-------+
Invoice table:
+------------+------------+------+------+----------+----------+
| invoice_id | product_id | rest | paid | canceled | refunded |
+------------+------------+------+------+----------+----------+
| 23         | 0          | 2    | 0    | 5        | 0        |
| 12         | 0          | 0    | 4    | 0        | 3        |
| 1          | 1          | 1    | 1    | 0        | 1        |
| 2          | 1          | 1    | 0    | 1        | 1        |
| 3          | 1          | 0    | 1    | 1        | 1        |
| 4          | 1          | 1    | 1    | 1        | 0        |
+------------+------------+------+------+----------+----------+
Result table:
+-------+------+------+----------+----------+
| name  | rest | paid | canceled | refunded |
+-------+------+------+----------+----------+
| bacon | 3    | 3    | 3        | 3        |
| ham   | 2    | 4    | 5        | 3        |
+-------+------+------+----------+----------+
- The amount of money left to pay for bacon is 1 + 1 + 0 + 1 = 3
- The amount of money paid for bacon is 1 + 0 + 1 + 1 = 3
- The amount of money canceled for bacon is 0 + 1 + 1 + 1 = 3
- The amount of money refunded for bacon is 1 + 1 + 1 + 0 = 3
- The amount of money left to pay for ham is 2 + 0 = 2
- The amount of money paid for ham is 0 + 4 = 4
- The amount of money canceled for ham is 5 + 0 = 5
- The amount of money refunded for ham is 0 + 3 = 3

*/

# V0
select p.name as name, 
    sum(i.rest) as rest,
    sum(i.paid) as paid, 
    sum(i.canceled) as canceled,
    sum(i.refunded) as refunded
from Invoice i
left join Product p on p.product_id = i.product_id
group by name
order by name

# V1
# https://circlecoder.com/products-worth-over-invoices/
select p.name as name, 
    sum(i.rest) as rest,
    sum(i.paid) as paid, 
    sum(i.canceled) as canceled,
    sum(i.refunded) as refunded
from Invoice i
left join Product p on p.product_id = i.product_id
group by name
order by name

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT name, 
       Sum(rest)     rest, 
       Sum(paid)     paid, 
       Sum(canceled) canceled, 
       Sum(refunded) refunded 
FROM   product p 
       INNER JOIN invoice i 
               ON p.product_id = i.product_id 
GROUP  BY name 
ORDER  BY name;