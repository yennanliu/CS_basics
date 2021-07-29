/*

Table: Products

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| new_price     | int     |
| change_date   | date    |
+---------------+---------+

(product_id, change_date) is the primary key of this table.
Each row of this table indicates that the price of some product was
changed to a new price at some date.
Write an SQL query to find the prices of all products on 2019-08-16.
 Assume the price of all products before any change is 10.

The query result format is in the following example:

Products table:
+------------+-----------+-------------+
| product_id | new_price | change_date |
+------------+-----------+-------------+
| 1          | 20        | 2019-08-14  |
| 2          | 50        | 2019-08-14  |
| 1          | 30        | 2019-08-15  |
| 1          | 35        | 2019-08-16  |
| 2          | 65        | 2019-08-17  |
| 3          | 20        | 2019-08-18  |
+------------+-----------+-------------+

Result table:
+------------+-------+
| product_id | price |
+------------+-------+
| 2          | 50    |
| 1          | 35    |
| 3          | 10    |
+------------+-------+

*/

# V0
# IDEA : UNION
#   -> CASE 1 : if the product changes price within before - 2019-08-16 => we find the latest price
#   -> CASE 2 : if the product NOT changes price within before - 2019-08-16 => we need to return 10 (as the default init price)
#   then we union case 1, and case 2
WITH cte1 AS
  (SELECT product_id,
          price
   FROM Products
   WHERE ((product_id,
           price) IN
            (SELECT product_id,
                    max(change_date) AS change_date
             FROM Products
             WHERE change_date < '2019-08-16'
             GROUP BY product_id)) ),
     cte2 AS (
SELECT product_id,
       10
FROM Products
WHERE (product_id NOT IN
         (SELECT product_id
          FROM Products
          WHERE change_date < '2019-08-16' ))
SELECT cte1.*
UNION cte2.*
ORDER BY price DESC;

# V1
# IDEA : UNION
#   -> CASE 1 : if the product changes price within before - 2019-08-16 => we find the latest price
#   -> CASE 2 : if the product NOT changes price within before - 2019-08-16 => we need to return 10 (as the default init price)
#   then we union case 1, and case 2
# https://blog.csdn.net/Hello_JavaScript/article/details/104611645
SELECT * 
FROM 
(SELECT product_id, new_price AS price
 FROM Products
 WHERE (product_id, change_date) IN (
                                     SELECT product_id, MAX(change_date)
                                     FROM Products
                                     WHERE change_date <= '2019-08-16'
                                     GROUP BY product_id)
 UNION
 SELECT DISTINCT product_id, 10 AS price
 FROM Products
 WHERE product_id NOT IN (SELECT product_id FROM Products WHERE change_date <= '2019-08-16')
) tmp
ORDER BY price DESC

# V1'
# https://code.dennyzhang.com/product-price-at-a-given-date
SELECT t1.product_id AS product_id,
       if(isnull(t2.price), 10, t2.price) AS price
FROM
  (SELECT DISTINCT product_id
   FROM Products) AS t1
LEFT JOIN
  (SELECT product_id,
          new_price AS price
   FROM Products
   WHERE (product_id,
          change_date) in
       (SELECT product_id,
               max(change_date)
        FROM Products
        WHERE change_date <='2019-08-16'
        GROUP BY product_id)) AS t2 ON t1.product_id = t2.product_id

# V2
# Time:  O(mlogn), m is the number of unique product id, n is the number of changed dates
# Space: O(m)
SELECT t1.product_id                      AS product_id, 
       IF(Isnull(t2.price), 10, t2.price) AS price 
FROM   (SELECT DISTINCT product_id 
        FROM   products) AS t1 
       left join (SELECT product_id, 
                         new_price AS price 
                  FROM   products 
                  WHERE  ( product_id, change_date ) IN (SELECT product_id, 
                                                                Max(change_date) 
                                                         FROM   products 
                                                         WHERE change_date <= '2019-08-16' 
                                                         GROUP  BY product_id)) 
                 AS t2 
              ON t1.product_id = t2.product_id 