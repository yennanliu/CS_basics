/*

SQL Schema
Table: Prices

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| start_date    | date    |
| end_date      | date    |
| price         | int     |
+---------------+---------+
(product_id, start_date, end_date) is the primary key for this table.
Each row of this table indicates the price of the product_id in the period from start_date to end_date.
For each product_id there will be no two overlapping periods. That means there will be no two intersecting periods for the same product_id.
Table: UnitsSold

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| purchase_date | date    |
| units         | int     |
+---------------+---------+
There is no primary key for this table, it may contain duplicates.
Each row of this table indicates the date, units and product_id of each product sold. 
Write an SQL query to find the average selling price for each product.

average_price should be rounded to 2 decimal places.

The query result format is in the following example:

Prices table:

+------------+------------+------------+--------+
| product_id | start_date | end_date   | price  |
+------------+------------+------------+--------+
| 1          | 2019-02-17 | 2019-02-28 | 5      |
| 1          | 2019-03-01 | 2019-03-22 | 20     |
| 2          | 2019-02-01 | 2019-02-20 | 15     |
| 2          | 2019-02-21 | 2019-03-31 | 30     |
+------------+------------+------------+--------+
UnitsSold table:

+------------+---------------+-------+
| product_id | purchase_date | units |
+------------+---------------+-------+
| 1          | 2019-02-25    | 100   |
| 1          | 2019-03-01    | 15    |
| 2          | 2019-02-10    | 200   |
| 2          | 2019-03-22    | 30    |
+------------+---------------+-------+
Result table:

+------------+---------------+
| product_id | average_price |
+------------+---------------+
| 1          | 6.96          |
| 2          | 16.96         |
+------------+---------------+
Average selling price = Total Price of Product / Number of products sold.
Average selling price for product 1 = ((100 * 5) + (15 * 20)) / 115 = 6.96
Average selling price for product 2 = ((200 * 15) + (30 * 30)) / 230 = 16.96

*/

# V0
SELECT a.product_id
	, round(SUM(a.units * b.price) / SUM(a.units), 2) AS average_price
FROM UnitsSold a
	JOIN Prices b
	ON (a.product_id = b.product_id
		AND a.purchase_date >= b.start_date
		AND a.purchase_date <= b.end_date)
group by product_id

# V0'
WITH cte1 AS
  (SELECT p.product_id,
          u.purchase_date,
          u.units,
          p.price
   FROM Prices p
   INNER JOIN UnitsSold u ON p.product_id = u.product_id
   WHERE u.purchase_date <= p.end_date
     AND u.purchase_date >= p.start_date )
SELECT product_id,
       ROUND(SUM(units * price) / sum(units), 2) AS average_price
FROM cte1
GROUP BY product_id;

# V1
# https://code.dennyzhang.com/average-selling-price
select UnitsSold.product_id, round(sum(units*price)/sum(units), 2) as average_price
from UnitsSold inner join Prices
on UnitsSold.product_id = Prices.product_id
and UnitsSold.purchase_date between Prices.start_date and Prices.end_date
group by UnitsSold.product_id

# V1'
# https://blog.csdn.net/Hello_JavaScript/article/details/104749332
select product_id,round(sum(a)/sum(units),2) as average_price from(
    select 
        p.product_id as product_id,
        price,units,
        price*units as a
    from Prices p 
    left join UnitsSold u
    on p.product_id=u.product_id and (purchase_date<=end_date and purchase_date>=start_date))t
    group by product_id

# V1''
# https://blog.csdn.net/Hello_JavaScript/article/details/104749332
SELECT a.product_id
	, round(SUM(a.units * b.price) / SUM(a.units), 2) AS average_price
FROM UnitsSold a
	JOIN Prices b
	ON (a.product_id = b.product_id
		AND a.purchase_date >= b.start_date
		AND a.purchase_date <= b.end_date)
group by product_id

# V2
# Time:  O(n)
# Space: O(n)
SELECT b.product_id, 
       ROUND(SUM(a.units * b.price) / SUM(a.units), 2) AS average_price 
FROM   UnitsSold AS a 
       INNER JOIN Prices AS b 
               ON a.product_id = b.product_id 
WHERE  a.purchase_date BETWEEN b.start_date AND b.end_date 
GROUP  BY product_id