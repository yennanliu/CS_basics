/*

Table: Customer

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| customer_id   | int     |
| name          | varchar |
| visited_on    | date    |
| amount        | int     |
+---------------+---------+
(customer_id, visited_on) is the primary key for this table.
This table contains data about customer transactions in a restaurant.
visited_on is the date on which the customer with ID (customer_id) have visited the restaurant.
amount is the total paid by a customer.
You are the restaurant owner and you want to analyze a possible expansion (there will be at least one customer every day).

Write an SQL query to compute moving average of how much customer paid in a 7 days window (current day + 6 days before) .

The query result format is in the following example:

Return result table ordered by visited_on.

average_amount should be rounded to 2 decimal places, all dates are in the format (‘YYYY-MM-DD’).

Customer table:
+-------------+--------------+--------------+-------------+
| customer_id | name         | visited_on   | amount      |
+-------------+--------------+--------------+-------------+
| 1           | Jhon         | 2019-01-01   | 100         |
| 2           | Daniel       | 2019-01-02   | 110         |
| 3           | Jade         | 2019-01-03   | 120         |
| 4           | Khaled       | 2019-01-04   | 130         |
| 5           | Winston      | 2019-01-05   | 110         | 
| 6           | Elvis        | 2019-01-06   | 140         | 
| 7           | Anna         | 2019-01-07   | 150         |
| 8           | Maria        | 2019-01-08   | 80          |
| 9           | Jaze         | 2019-01-09   | 110         | 
| 1           | Jhon         | 2019-01-10   | 130         | 
| 3           | Jade         | 2019-01-10   | 150         | 
+-------------+--------------+--------------+-------------+

Result table:
+--------------+--------------+----------------+
| visited_on   | amount       | average_amount |
+--------------+--------------+----------------+
| 2019-01-07   | 860          | 122.86         |
| 2019-01-08   | 840          | 120            |
| 2019-01-09   | 840          | 120            |
| 2019-01-10   | 1000         | 142.86         |
+--------------+--------------+----------------+

1st moving average from 2019-01-01 to 2019-01-07 has an average_amount of (100 + 110 + 120 + 130 + 110 + 140 + 150)/7 = 122.86
2nd moving average from 2019-01-02 to 2019-01-08 has an average_amount of (110 + 120 + 130 + 110 + 140 + 150 + 80)/7 = 120
3rd moving average from 2019-01-03 to 2019-01-09 has an average_amount of (120 + 130 + 110 + 140 + 150 + 80 + 110)/7 = 120
4th moving average from 2019-01-04 to 2019-01-10 has an average_amount o

*/

# V0
SELECT t1.visited_on, sum(t2.amount) amount, ROUND(SUM(t2.amount)/7.0, 2) average_amount
FROM
(SELECT DISTINCT visited_on FROM customer
WHERE visited_on >= DATE_ADD((SELECT min(visited_on) FROM customer), INTERVAL 6 DAY)) t1
LEFT JOIN
customer t2
ON t1.visited_on <= DATE_ADD(t2.visited_on, INTERVAL 6 DAY) AND t1.visited_on >= t2.visited_on
GROUP BY t1.visited_on

# V1
# https://blog.csdn.net/Hello_JavaScript/article/details/104783384
SELECT t1.visited_on, sum(t2.amount) amount, ROUND(SUM(t2.amount)/7.0, 2) average_amount
FROM
(SELECT DISTINCT visited_on FROM customer
WHERE visited_on >= DATE_ADD((SELECT min(visited_on) FROM customer), INTERVAL 6 DAY)) t1
LEFT JOIN
customer t2
ON t1.visited_on <= DATE_ADD(t2.visited_on, INTERVAL 6 DAY) AND t1.visited_on >= t2.visited_on
GROUP BY t1.visited_on

# V1'
# https://blog.csdn.net/Hello_JavaScript/article/details/104783384
select c1.visited_on,sum(c2.amount) amount,round(sum(c2.amount)/7,2) average_amount
from 
(select customer_id,name,visited_on,sum(amount) amount from Customer group by visited_on)c1 cross join 
(select customer_id,name,visited_on,sum(amount) amount from Customer group by visited_on) c2 
where datediff(c1.visited_on,c2.visited_on)<7  and datediff(c1.visited_on,c2.visited_on)>=0
group by c1.visited_on
having count(*)>6
GROUP BY t1.visited_on

# V1''
# https://code.dennyzhang.com/restaurant-growth
select t1.visited_on, 
    sum(t2.amount) as amount,
    round(avg(t2.amount), 2) as average_amount
from (
    select visited_on, sum(amount) as amount
    from Customer
    group by visited_on) as t1
    inner join
     (
    select visited_on, sum(amount) as amount
    from Customer
    group by visited_on) as t2
on t2.visited_on between DATE_SUB(t1.visited_on, INTERVAL 6 DAY) and t1.visited_on
group by t1.visited_on
having count(1)=7

# V2
# Time:  O(nlogn)
# Space: O(n)
WITH total_cte AS
(
  SELECT DISTINCT visited_on, 
         SUM(amount) OVER (ORDER BY visited_on
                           RANGE BETWEEN INTERVAL 6 DAY PRECEDING
                           AND CURRENT ROW) AS amount
  FROM Customer
)

SELECT visited_on, amount, ROUND(amount/7, 2) AS average_amount
FROM total_cte AS a
INNER JOIN
(SELECT MIN(visited_on) AS min_visited_on FROM total_cte) AS b
ON DATEDIFF(visited_on, min_visited_on) >= 6;