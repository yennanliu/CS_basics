/*

Description
LeetCode Problem 1795.

Table: Products

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| product_id  | int     |
| store1      | int     |
| store2      | int     |
| store3      | int     |
+-------------+---------+
product_id is the primary key for this table.
Each row in this table indicates the product's price in 3 different stores: store1, store2, and store3.
If the product is not available in a store, the price will be null in that store's column.
Write an SQL query to rearrange the Products table so that each row has (product_id, store, price). If a product is not available in a store, do not include a row with that product_id and store combination in the result table.

Return the result table in any order.

The query result format is in the following example:

Products table:
+------------+--------+--------+--------+
| product_id | store1 | store2 | store3 |
+------------+--------+--------+--------+
| 0          | 95     | 100    | 105    |
| 1          | 70     | null   | 80     |
+------------+--------+--------+--------+

Result table:
+------------+--------+-------+
| product_id | store  | price |
+------------+--------+-------+
| 0          | store1 | 95    |
| 0          | store2 | 100   |
| 0          | store3 | 105   |
| 1          | store1 | 70    |
| 1          | store3 | 80    |
+------------+--------+-------+

Product 0 is available in all three stores with prices 95, 100, and 105 respectively.
Product 1 is available in store1 with price 70 and store3 with price 80. The product is not available in store2.

*/

# V0
SELECT product_id,
       'store1' AS store,
       store1   AS price
FROM   products
WHERE  store1 IS NOT NULL
UNION ALL
SELECT product_id,
       'store2' AS store,
       store2   AS price
FROM   products
WHERE  store2 IS NOT NULL
UNION ALL
SELECT product_id,
       'store3' AS store,
       store3   AS price
FROM   products
WHERE  store3 IS NOT NULL;

# V1
# https://circlecoder.com/rearrange-products-table/
select product_id,'store1' as store, store1 as price 
from products 
where store1 is not null

union

select product_id,'store2' as store, store2 as price 
from products 
where store2 is not null

union

select product_id,'store3' as store, store3 as price 
from products 
where store3 is not null

# V2
# Time:  O(n)
# Space: O(n)
SELECT product_id,
       'store1' AS store,
       store1   AS price
FROM   products
WHERE  store1 IS NOT NULL
UNION ALL
SELECT product_id,
       'store2' AS store,
       store2   AS price
FROM   products
WHERE  store2 IS NOT NULL
UNION ALL
SELECT product_id,
       'store3' AS store,
       store3   AS price
FROM   products
WHERE  store3 IS NOT NULL;