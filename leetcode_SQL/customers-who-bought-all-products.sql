/* 

https://code.dennyzhang.com/customers-who-bought-all-products

LeetCode: Customers Who Bought All Products

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| customer_id | int     |
| product_key | int     |
+-------------+---------+
product_key is a foreign key to Product table.
Table: Product

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| product_key | int     |
+-------------+---------+
product_key is the primary key column for this table.
Write an SQL query for a report that provides the customer ids from the Customer table that bought all the products in the Product table.

For example:

Customer table:
+-------------+-------------+
| customer_id | product_key |
+-------------+-------------+
| 1           | 5           |
| 2           | 6           |
| 3           | 5           |
| 3           | 6           |
| 1           | 6           |
+-------------+-------------+

Product table:
+-------------+
| product_key |
+-------------+
| 5           |
| 6           |
+-------------+

Result table:
+-------------+
| customer_id |
+-------------+
| 1           |
| 3           |
+-------------+
The customers who bought all the products (5 and 6) are customers with id 1 and 3.

*/

# V0 
select customer_id
from Customer
group by customer_id
having count(distinct product_key) = (
    select count(1)
    from Product)

# V1 
# https://code.dennyzhang.com/customers-who-bought-all-products
select customer_id
from Customer
group by customer_id
having count(distinct product_key) = (
    select count(1)
    from Product)

# V2 
# Time:  O(n + k), n is number of customer, k is number of product
# Space: O(n + k)
SELECT customer_id
FROM customer
GROUP BY customer_id
HAVING count(DISTINCT product_key)=
  (SELECT count(DISTINCT product_key)
   FROM product)
ORDER BY NULL