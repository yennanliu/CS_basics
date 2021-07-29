/*

Table: Users

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| user_id        | int     |
| join_date      | date    |
| favorite_brand | varchar |
+----------------+---------+

user_id is the primary key of this table.
This table has the info of the users of an online shopping 
website where users can sell and buy items.
Table: Orders

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| order_id      | int     |
| order_date    | date    |
| item_id       | int     |
| buyer_id      | int     |
| seller_id     | int     |
+---------------+---------+
order_id is the primary key of this table.
item_id is a foreign key to the Items table.
buyer_id and seller_id are foreign keys to the Users table.
Table: Items

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| item_id       | int     |
| item_brand    | varchar |
+---------------+---------+

item_id is the primary key of this table.
Write an SQL query to find for each user, whether 
the brand of the second item (by date) they sold is their 
favorite brand. If a user sold less than two items, 
report the answer for that user as no.

It is guaranteed that no seller sold more than one item on a day.

The query result format is in the following example:

Users table:
+---------+------------+----------------+
| user_id | join_date  | favorite_brand |
+---------+------------+----------------+
| 1       | 2019-01-01 | Lenovo         |
| 2       | 2019-02-09 | Samsung        |
| 3       | 2019-01-19 | LG             |
| 4       | 2019-05-21 | HP             |
+---------+------------+----------------+

Orders table:
+----------+------------+---------+----------+-----------+
| order_id | order_date | item_id | buyer_id | seller_id |
+----------+------------+---------+----------+-----------+
| 1        | 2019-08-01 | 4       | 1        | 2         |
| 2        | 2019-08-02 | 2       | 1        | 3         |
| 3        | 2019-08-03 | 3       | 2        | 3         |
| 4        | 2019-08-04 | 1       | 4        | 2         |
| 5        | 2019-08-04 | 1       | 3        | 4         |
| 6        | 2019-08-05 | 2       | 2        | 4         |
+----------+------------+---------+----------+-----------+

Items table:
+---------+------------+
| item_id | item_brand |
+---------+------------+
| 1       | Samsung    |
| 2       | Lenovo     |
| 3       | LG         |
| 4       | HP         |
+---------+------------+

Result table:
+-----------+--------------------+
| seller_id | 2nd_item_fav_brand |
+-----------+--------------------+
| 1         | no                 |
| 2         | yes                |
| 3         | yes                |
| 4         | no                 |
+-----------+--------------------+

The answer for the user with id 1 is no because they sold nothing.
The answer for the users with id 2 and 3 is yes because the brands of their second sold items are their favorite brands.
The answer for the user with id 4 is no because the brand of their second sold item is not their favorite brand.

*/

# V0
WITH cte1 AS
  (SELECT seller_id,
          item_id,
          rank() over(PARTITION BY seller_id
                      ORDER BY order_date) AS second_item
   FROM Orders),
     cte2 AS
  (SELECT c1.seller_id,
          i.item_brand
   FROM Items i
   JOIN cte1 c1 ON i.item_id=c1.item_id
   WHERE c1.second_item=2 )
SELECT u.user_id AS seller_id,
       CASE
           WHEN u.favorite_brand = c2.item_brand THEN 'yes'
           ELSE 'no'
       END AS '2nd_item_fav_brand'
FROM Users u
LEFT JOIN cte2 c2 ON u.user_id=c2.seller_id
ORDER BY c2.seller_id

# V1
# https://blog.csdn.net/chelseajcole/article/details/104726746
WITH cte1 AS
  (SELECT seller_id,
          item_id,
          rank() over(PARTITION BY seller_id
                      ORDER BY order_date) AS second_item
   FROM Orders),
     cte2 AS
  (SELECT c1.seller_id,
          i.item_brand
   FROM Items i
   JOIN cte1 c1 ON i.item_id=c1.item_id
   WHERE c1.second_item=2 )
SELECT u.user_id AS seller_id,
       CASE
           WHEN u.favorite_brand = c2.item_brand THEN 'yes'
           ELSE 'no'
       END AS '2nd_item_fav_brand'
FROM Users u
LEFT JOIN cte2 c2 ON u.user_id=c2.seller_id
ORDER BY c2.seller_id

# V1'
# https://code.dennyzhang.com/market-analysis-ii
SELECT user_id AS seller_id,
       if(isnull(item_brand), "no", "yes") AS 2nd_item_fav_brand
FROM Users
LEFT JOIN
  (SELECT seller_id,
          item_brand
   FROM Orders
   INNER JOIN Items ON Orders.item_id = Items.item_id
   WHERE (seller_id,
          order_date) in
       (SELECT seller_id,
               min(order_date) AS order_date
        FROM Orders
        WHERE (seller_id,
               order_date) not in
            (SELECT seller_id,
                    min(order_date)
             FROM Orders
             GROUP BY seller_id)
        GROUP BY seller_id) ) AS t ON Users.user_id = t.seller_id
AND favorite_brand = item_brand

# V2
# Time:  O(m + n), m is the number of users, n is the number of orders
# Space: O(m + n)
SELECT user_id AS seller_id, 
       IF(item_brand = favorite_brand, 'yes', 'no') AS 2nd_item_fav_brand 
FROM   (SELECT user_id, 
               favorite_brand, 
               (SELECT    item_id
                FROM      orders o 
                WHERE     user_id = o.seller_id 
                ORDER BY order_date limit 1, 1) item_id
        FROM   users) u
        LEFT JOIN items i 
        ON        u.item_id = i.item_id 