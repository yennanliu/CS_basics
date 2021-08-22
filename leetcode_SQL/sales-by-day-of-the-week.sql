/*

1479. Sales by Day of the Week
Table: Orders
+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| order_id      | int     |
| customer_id   | int     |
| order_date    | date    | 
| item_id       | varchar |
| quantity      | int     |
+---------------+---------+
(ordered_id, item_id) is the primary key for this table.
This table contains information of the orders placed.
order_date is the date when item_id was ordered by the customer with id customer_id.
Table: Items
+---------------------+---------+
| Column Name         | Type    |
+---------------------+---------+
| item_id             | varchar |
| item_name           | varchar |
| item_category       | varchar |
+---------------------+---------+
item_id is the primary key for this table.
item_name is the name of the item.
item_category is the category of the item.
You are the business owner and would like to obtain a sales report for category items and day of the week.
Write an SQL query to report how many units in each category have been ordered on each day of the week.
Return the result table ordered by category.
The query result format is in the following example:
Orders table:
+------------+--------------+-------------+--------------+-------------+
| order_id   | customer_id  | order_date  | item_id      | quantity    |
+------------+--------------+-------------+--------------+-------------+
| 1          | 1            | 2020-06-01  | 1            | 10          |
| 2          | 1            | 2020-06-08  | 2            | 10          |
| 3          | 2            | 2020-06-02  | 1            | 5           |
| 4          | 3            | 2020-06-03  | 3            | 5           |
| 5          | 4            | 2020-06-04  | 4            | 1           |
| 6          | 4            | 2020-06-05  | 5            | 5           |
| 7          | 5            | 2020-06-05  | 1            | 10          |
| 8          | 5            | 2020-06-14  | 4            | 5           |
| 9          | 5            | 2020-06-21  | 3            | 5           |
+------------+--------------+-------------+--------------+-------------+
Items table:
+------------+----------------+---------------+
| item_id    | item_name      | item_category |
+------------+----------------+---------------+
| 1          | LC Alg. Book   | Book          |
| 2          | LC DB. Book    | Book          |
| 3          | LC SmarthPhone | Phone         |
| 4          | LC Phone 2020  | Phone         |
| 5          | LC SmartGlass  | Glasses       |
| 6          | LC T-Shirt XL  | T-Shirt       |
+------------+----------------+---------------+
Result table:
+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
| Category   | Monday    | Tuesday   | Wednesday | Thursday  | Friday    | Saturday  | Sunday    |
+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
| Book       | 20        | 5         | 0         | 0         | 10        | 0         | 0         |
| Glasses    | 0         | 0         | 0         | 0         | 5         | 0         | 0         |
| Phone      | 0         | 0         | 5         | 1         | 0         | 0         | 10        |
| T-Shirt    | 0         | 0         | 0         | 0         | 0         | 0         | 0         |
+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
On Monday (2020-06-01, 2020-06-08) were sold a total of 20 units (10 + 10) in the category Book (ids: 1, 2).
On Tuesday (2020-06-02) were sold a total of 5 units  in the category Book (ids: 1, 2).
On Wednesday (2020-06-03) were sold a total of 5 units in the category Phone (ids: 3, 4).
On Thursday (2020-06-04) were sold a total of 1 unit in the category Phone (ids: 3, 4).
On Friday (2020-06-05) were sold 10 units in the category Book (ids: 1, 2) and 5 units in Glasses (ids: 5).
On Saturday there are no items sold.
On Sunday (2020-06-14, 2020-06-21) were sold a total of 10 units (5 +5) in the category Phone (ids: 3, 4).
There are no sales of T-Shirt.

*/


# V0

# V1
# https://medium.com/jen-li-chen-in-data-science/leetcode-sql-987dc052830c
select i.item_category as Category,
       coalesce(sum(case when dayofweek(o.order_date) = 2 then o.quantity end), 0) as Monday,
coalesce(sum(case when dayofweek(o.order_date) = 3 then o.quantity end), 0) as Tuesday,
coalesce(sum(case when dayofweek(o.order_date) = 4 then o.quantity end), 0) as Wednesday,
coalesce(sum(case when dayofweek(o.order_date) = 5 then o.quantity end), 0) as Thursday,
coalesce(sum(case when dayofweek(o.order_date) = 6 then o.quantity end), 0) as Friday,
coalesce(sum(case when dayofweek(o.order_date) = 7 then o.quantity end), 0) as Saturday,
coalesce(sum(case when dayofweek(o.order_date) = 1 then o.quantity end), 0) as Sunday
from Items i
left join
Orders o
on i.item_id = o.item_id
group by 1
order by 1
;

# V1'
# https://zhuanlan.zhihu.com/p/264966908
# https://blog.csdn.net/Changxing_J/article/details/110426678
SELECT a.item_category AS Category,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 2 THEN b.quantity ELSE 0 END) AS Monday,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 3 THEN b.quantity ELSE 0 END) AS Tuesday,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 4 THEN b.quantity ELSE 0 END) AS Wednesday,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 5 THEN b.quantity ELSE 0 END) AS Thursday,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 6 THEN b.quantity ELSE 0 END) AS Friday,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 7 THEN b.quantity ELSE 0 END) AS Saturday,
SUM(CASE WHEN DAYOFWEEK(b.order_date) = 1 THEN b.quantity ELSE 0 END) AS Sunday
FROM Items AS a
LEFT JOIN Orders AS b
ON a.item_id = b.item_id
GROUP BY a.item_category
ORDER BY a.item_category;

# V2
# Time:  O(m + n)
# Space: O(n)
SELECT a.item_category AS 'CATEGORY',
       sum(CASE
               WHEN weekday(b.order_date) = 0 THEN b.quantity
               ELSE 0
           END) AS 'MONDAY',
       sum(CASE
               WHEN weekday(b.order_date) = 1 THEN b.quantity
               ELSE 0
           END) AS 'TUESDAY',
       sum(CASE
               WHEN weekday(b.order_date) = 2 THEN b.quantity
               ELSE 0
           END) AS 'WEDNESDAY',
       sum(CASE
               WHEN weekday(b.order_date) = 3 THEN b.quantity
               ELSE 0
           END) AS 'THURSDAY',
       sum(CASE
               WHEN weekday(b.order_date) = 4 THEN b.quantity
               ELSE 0
           END) AS 'FRIDAY',
       sum(CASE
               WHEN weekday(b.order_date) = 5 THEN b.quantity
               ELSE 0
           END) AS 'SATURDAY',
       sum(CASE
               WHEN weekday(b.order_date) = 6 THEN b.quantity
               ELSE 0
           END) AS 'SUNDAY'
FROM items a
LEFT JOIN orders b ON a.item_id = b.item_id
GROUP BY a.item_category
ORDER BY a.item_category;