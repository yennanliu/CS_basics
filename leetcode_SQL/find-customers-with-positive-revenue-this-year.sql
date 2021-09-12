/*


Share

Find Customers With Positive Revenue this Year Problem

Description
LeetCode Problem 1821.

Table: Customers

+--------------+------+
| Column Name  | Type |
+--------------+------+
| customer_id  | int  |
| year         | int  |
| revenue      | int  |
+--------------+------+
(customer_id, year) is the primary key for this table.
This table contains the customer ID and the revenue of customers in different years.
Note that this revenue can be negative.
Write an SQL query to report the customers with postive revenue in the year 2021.

Return the result table in any order.

The query result format is in the following example:

Customers
+-------------+------+---------+
| customer_id | year | revenue |
+-------------+------+---------+
| 1           | 2018 | 50      |
| 1           | 2021 | 30      |
| 1           | 2020 | 70      |
| 2           | 2021 | -50     |
| 3           | 2018 | 10      |
| 3           | 2016 | 50      |
| 4           | 2021 | 20      |
+-------------+------+---------+

Result table:
+-------------+
| customer_id |
+-------------+
| 1           |
| 4           |
+-------------+

Customer 1 has revenue equal to 30 in year 2021.
Customer 2 has revenue equal to -50 in year 2021.
Customer 3 has no revenue in year 2021.
Customer 4 has revenue equal to 20 in year 2021.
Thus only customers 1 and 4 have postive revenue in year 2021.

*/

# V0

# V1
# https://circlecoder.com/find-customers-with-positive-revenue-this-year/
select  customer_id
from Customers
where year = 2021
group by customer_id,year
having sum(revenue) > 0 

# V2
# Time:  O(n)
# Space: O(n)
SELECT customer_id
FROM customers
WHERE YEAR = 2021 AND revenue > 0;