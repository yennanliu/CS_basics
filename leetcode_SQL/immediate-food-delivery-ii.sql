/*

1174. Immediate Food Delivery II
https://leetcode.com/problems/immediate-food-delivery-ii/
Medium

Table: Delivery

+-----------------------------+---------+
| Column Name                 | Type    |
+-----------------------------+---------+
| delivery_id                 | int     |
| customer_id                 | int     |
| order_date                  | date    |
| customer_pref_delivery_date | date    |
+-----------------------------+---------+
delivery_id is the column of unique values of this table.
The table holds information about food delivery to customers that make orders at some date and specify a preferred delivery date (on the same order date or after it).

If the customer's preferred delivery date is the same as the order date, then the order is called immediate; otherwise, it is called scheduled.

The first order of a customer is the order with the earliest order date that the customer made. It is guaranteed that a customer has precisely one first order.

Write a solution to find the percentage of immediate orders in the first orders of all customers, rounded to 2 decimal places.

The result format is in the following example.

Example 1:

Input:
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
Output:
+----------------------+
| immediate_percentage |
+----------------------+
| 50.00                |
+----------------------+
Explanation:
The customer id 1 has a first order with delivery id 1 and it is scheduled.
The customer id 2 has a first order with delivery id 2 and it is immediate.
The customer id 3 has a first order with delivery id 5 and it is scheduled.
The customer id 4 has a first order with delivery id 7 and it is immediate.
Hence, half the customers have immediate first orders.

*/

# V0
# IDEA : FIND EACH CUSTOMER'S FIRST ORDER DATE, JOIN BACK, SHARE OF IMMEDIATE ONES
#
#   Step 1: MIN(order_date) per customer is the first order's date.
#   Step 2: join back on (customer_id, order_date) to get that order's row --
#   the problem guarantees exactly one first order, so one row per customer.
#   Step 3: the share of those rows whose preferred date equals the order date.
#
# time = O(n), space = O(c)    -- n = rows in Delivery, c = customers; one GROUP BY, one hash join
WITH first_order AS (
    -- one row per customer: the date of their first order
    SELECT
        customer_id,
        MIN(order_date) AS first_date
    FROM Delivery
    GROUP BY customer_id
)
SELECT
    ROUND(
        SUM(CASE WHEN d.order_date = d.customer_pref_delivery_date THEN 1 ELSE 0 END) * 100
            / COUNT(*),
        2
    ) AS immediate_percentage
FROM Delivery AS d
JOIN first_order AS f
    ON  f.customer_id = d.customer_id
    AND f.first_date = d.order_date;

# V1
# IDEA : NUMBER EACH CUSTOMER'S ORDERS WITH ROW_NUMBER, KEEP NUMBER 1
#
#   ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY order_date) numbers
#   each customer's orders oldest first; number 1 is the first order.
#   ROW_NUMBER (not RANK) so a customer can never contribute two rows, even if
#   a tie on order_date slipped past the guarantee.
#
# time = O(n log n), space = O(n)    -- n = rows in Delivery; the window sorts within each customer
WITH ranked AS (
    SELECT
        order_date,
        customer_pref_delivery_date,
        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY order_date
        ) AS order_rank
    FROM Delivery
)
SELECT
    ROUND(
        SUM(CASE WHEN order_date = customer_pref_delivery_date THEN 1 ELSE 0 END) * 100
            / COUNT(*),
        2
    ) AS immediate_percentage
FROM ranked
WHERE order_rank = 1;
