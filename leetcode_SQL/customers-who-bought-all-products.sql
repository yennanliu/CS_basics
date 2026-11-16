/*

1045. Customers Who Bought All Products
https://leetcode.com/problems/customers-who-bought-all-products/
Medium

Table: Customer

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| customer_id | int     |
| product_key | int     |
+-------------+---------+
This table may contain duplicates rows.
customer_id is not NULL.
product_key is a foreign key (reference column) to Product table.

Table: Product

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| product_key | int     |
+-------------+---------+
product_key is the primary key (column with unique values) for this table.

Write a solution to report the customer ids from the Customer table that bought all the products in the Product table.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
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
Output:
+-------------+
| customer_id |
+-------------+
| 1           |
| 3           |
+-------------+
Explanation:
The customers who bought all the products (5 and 6) are customers with IDs 1 and 3.

*/

# V0
# IDEA : RELATIONAL DIVISION BY COUNTING -- DISTINCT PRODUCTS = ALL PRODUCTS
#
#   A customer bought every product iff the number of DISTINCT products they
#   bought equals the size of Product. DISTINCT is required: Customer may hold
#   duplicate rows, and buying product 5 twice must not stand in for 6.
#   It relies on product_key being a foreign key into Product, so a customer
#   cannot reach the count with a key Product does not have. COUNT(DISTINCT
#   product_key) would also skip a NULL product_key.
#
# time = O(n + m), space = O(n)    -- n = rows in Customer, m = rows in Product; one hash group, one count
SELECT customer_id
FROM Customer
GROUP BY customer_id
HAVING COUNT(DISTINCT product_key) = (
    SELECT COUNT(product_key)   -- product_key is the PK: no NULLs, no duplicates
    FROM Product
);

# V1
# IDEA : RELATIONAL DIVISION BY DOUBLE NEGATION -- NO PRODUCT THE CUSTOMER LACKS
#
#   Keep a customer when there is NO product for which there is NO matching
#   purchase. It does not lean on the foreign key or on counting, so it stays
#   correct even if Customer held keys that are not in Product -- the
#   textbook form of "bought all".
#
# time = O(c * m), space = O(c)    -- c = distinct customers, m = products; each pair probes Customer (indexed)
WITH customers AS (
    SELECT DISTINCT customer_id
    FROM Customer
)
SELECT c.customer_id
FROM customers AS c
WHERE NOT EXISTS (
    -- a product this customer never bought
    SELECT p.product_key
    FROM Product AS p
    WHERE NOT EXISTS (
        SELECT cu.customer_id
        FROM Customer AS cu
        WHERE cu.customer_id = c.customer_id
          AND cu.product_key = p.product_key
    )
);
