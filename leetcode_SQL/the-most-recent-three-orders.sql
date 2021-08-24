/*

1532. The Most Recent Three Orders
Table: Customers
+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| customer_id   | int     |
| name          | varchar |
+---------------+---------+
customer_id is the primary key for this table.
This table contains information about customers.
Table: Orders
+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| order_id      | int     |
| order_date    | date    |
| customer_id   | int     |
| cost          | int     |
+---------------+---------+
order_id is the primary key for this table.
This table contains information about the orders made by customer_id.
Each customer has one order per day.
Write an SQL query to find the most recent 3 orders of each user. If a user ordered less than 3 orders return all of their orders.
Return the result table sorted by customer_name in ascending order and in case of a tie by the customer_id in ascending order. If there still a tie, order them by the order_date in descending order.
The query result format is in the following example:
Customers
+-------------+-----------+
| customer_id | name      |
+-------------+-----------+
| 1           | Winston   |
| 2           | Jonathan  |
| 3           | Annabelle |
| 4           | Marwan    |
| 5           | Khaled    |
+-------------+-----------+
Orders
+----------+------------+-------------+------+
| order_id | order_date | customer_id | cost |
+----------+------------+-------------+------+
| 1        | 2020-07-31 | 1           | 30   |
| 2        | 2020-07-30 | 2           | 40   |
| 3        | 2020-07-31 | 3           | 70   |
| 4        | 2020-07-29 | 4           | 100  |
| 5        | 2020-06-10 | 1           | 1010 |
| 6        | 2020-08-01 | 2           | 102  |
| 7        | 2020-08-01 | 3           | 111  |
| 8        | 2020-08-03 | 1           | 99   |
| 9        | 2020-08-07 | 2           | 32   |
| 10       | 2020-07-15 | 1           | 2    |
+----------+------------+-------------+------+
Result table:
+---------------+-------------+----------+------------+
| customer_name | customer_id | order_id | order_date |
+---------------+-------------+----------+------------+
| Annabelle     | 3           | 7        | 2020-08-01 |
| Annabelle     | 3           | 3        | 2020-07-31 |
| Jonathan      | 2           | 9        | 2020-08-07 |
| Jonathan      | 2           | 6        | 2020-08-01 |
| Jonathan      | 2           | 2        | 2020-07-30 |
| Marwan        | 4           | 4        | 2020-07-29 |
| Winston       | 1           | 8        | 2020-08-03 |
| Winston       | 1           | 1        | 2020-07-31 |
| Winston       | 1           | 10       | 2020-07-15 |
+---------------+-------------+----------+------------+
Winston has 4 orders, we discard the order of "2020-06-10" because it is the oldest order.
Annabelle has only 2 orders, we return them.
Jonathan has exactly 3 orders.
Marwan ordered only one time.
We sort the result table by customer_name in ascending order, by customer_id in ascending order and by order_date in descending order in case of a tie.
Follow-up:
Can you write a general solution for the most recent n orders?
Note: left join is faster than inner join

*/

# V0
SELECT name customer_name, customer_id, order_id, order_date
FROM
(
	SELECT Orders.*, Customers.name,
		RANK() over (PARTITION by Orders.customer_id ORDER BY order_date desc) rk
	FROM Orders, Customers
	WHERE Orders.customer_id = Customers.customer_id 
) t
WHERE rk<=3
ORDER BY 1,2,4 desc

# V1
# http://blog.csdn.net/qq_21201267/article/details/107680682

# V1'
# https://github.com/ladychili/leetcode/blob/master/sql/1532.the-most-recent-three-orders.md
SELECT name customer_name, customer_id, order_id, order_date
FROM
(
	SELECT Orders.*, Customers.name,
		RANK() over (PARTITION by Orders.customer_id ORDER BY order_date desc) rk
	FROM Orders, Customers
	WHERE Orders.customer_id = Customers.customer_id 
) t
WHERE rk<=3
ORDER BY 1,2,4 desc

# V1''
# https://medium.com/jen-li-chen-in-data-science/leetcode-sql-5878916093b7
select customer_name, customer_id, order_id, order_date
from
(
select name as customer_name, o.customer_id, order_id,order_date,
         dense_rank() over(partition by customer_id order by order_date desc) as rnk
from Orders o
left join
Customers c
using(customer_id)
) t
where rnk <= n
order by 1, 2, 4 desc;

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT customer_name,
       customer_id,
       order_id,
       order_date
FROM
  (SELECT @accu := (CASE
                        WHEN co.customer_id = @prev THEN @accu + 1
                        ELSE 1
                    END) AS n,
          @prev := co.customer_id AS customer_id,
          co.name AS customer_name,
          co.order_id AS order_id,
          co.order_date AS order_date
   FROM
     (SELECT @accu := 0, @prev := 0) AS init,
     (SELECT c.name, c.customer_id, o.order_id, o.order_date FROM
      Customers c
      INNER JOIN Orders o
      ON c.customer_id = o.customer_id
      ORDER BY c.name ASC, c.customer_id ASC, o.order_date DESC) AS co
  ) AS tmp
WHERE n < 4;