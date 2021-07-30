/*

Table: Delivery

+-----------------------------+---------+
| Column Name                 | Type    |
+-----------------------------+---------+
| delivery_id                 | int     |
| customer_id                 | int     |
| order_date                  | date    |
| customer_pref_delivery_date | date    |
+-----------------------------+---------+

delivery_id is the primary key of this table.
The table holds information about food delivery to customers that make orders at some date and specify a preferred delivery date (on the same order date or after it).
If the preferred delivery date of the customer is the same as the order date then the order is called immediate otherwise it’s called scheduled.

The first order of a customer is the order with the earliest order date that customer made.

Write an SQL query to find the percentage of immediate orders in the first orders of all customers, rounded to 2 decimal places.

The query result format is in the following example:

Delivery table:
+-------------+-------------+------------+-----------------------------+
| delivery_id | customer_id | order_date | customer_pref_delivery_date |
+-------------+-------------+------------+-----------------------------+
| 1           | 1           | 2019-08-01 | 2019-08-02                  |
| 2           | 2           | 2019-08-02 | 2019-08-02                  |
| 3           | 1           | 2019-08-11 | 2019-08-12                  |
| 4           | 3           | 2019-08-24 | 2019-08-24                  |
| 5           | 3           | 2019-08-21 | 2019-08-22                  |
| 6           | 2           | 2019-08-11 | 2019-08-13                  |
| 7           | 4           | 2019-08-09 | 2019-08-09                  |
+-------------+-------------+------------+-----------------------------+

Result table:
+----------------------+
| immediate_percentage |
+----------------------+
| 50.00                |
+----------------------+
The customer id 1 has a first order with delivery id 1 and it is scheduled.
The customer id 2 has a first order with delivery id 2 and it is immediate.
The customer id 3 has a first order with delivery id 5 and it is scheduled.
The customer id 4 has a first order with delivery id 7 and it is immediate.
Hence, half the customers have immediate first orders.

*/

# V0
WITH cte1 AS (
SELECT customer_id,
       order_date,
       customer_pref_delivery_date
FROM Delivery
WHERE ((customer_id,
        order_date) IN
         (SELECT customer_id,
                 min(order_date)
          FROM Delivery
          GROUP BY customer_id))
  SELECT ROUND(100 * (SUM(CASE
                              WHEN cte1.order_date = cte1.customer_pref_delivery_date THEN 1
                              ELSE 0
                          END) / COUNT(delivery_id)), 2)
  FROM Delivery


# V1
# https://blog.csdn.net/Hello_JavaScript/article/details/104625257
select round(100*count(*)/@num_cus,2) immediate_percentage
from Delivery d join (
    select customer_id,min(order_date) order_date
    from Delivery
    group by customer_id
)  w on d.customer_id=w.customer_id and d.order_date=w.order_date
,(select @num_cus:=count(distinct customer_id) from Delivery) t
where d.order_date=d.customer_pref_delivery_date;

# V1
# https://blog.csdn.net/Hello_JavaScript/article/details/104625257
select round(sum(tag)/count(id),2) as immediate_percentage
from
(    
    select customer_id as id, case
    when min(order_date) = min(customer_pref_delivery_date)
    then 100.00
    else 0 end as tag
    from Delivery d1
    group by customer_id
) d2;

# V1''
# https://code.dennyzhang.com/immediate-food-delivery-ii
select round(100*sum(case when order_date=customer_pref_delivery_date then 1 else 0 end)/count(distinct customer_id), 2) immediate_percentage 
from Delivery
where (customer_id, order_date) in 
    (select customer_id, min(order_date)
    from Delivery
    group by customer_id)

# V2
# Time:  O(n), n is the number of delivery
# Space: O(m), m is the number of customer
SELECT Round(100 * Sum(order_date = customer_pref_delivery_date) / 
                    Count(DISTINCT customer_id), 2) AS immediate_percentage 
FROM   delivery 
WHERE  ( customer_id, order_date ) IN (SELECT customer_id, 
                                              Min(order_date) 
                                       FROM   delivery 
                                       GROUP  BY customer_id) 