/*

1321. Restaurant Growth
https://leetcode.com/problems/restaurant-growth/
Medium

Table: Customer

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| customer_id   | int     |
| name          | varchar |
| visited_on    | date    |
| amount        | int     |
+---------------+---------+
In SQL,(customer_id, visited_on) is the primary key for this table.
This table contains data about customer transactions in a restaurant.
visited_on is the date on which the customer with ID (customer_id) has visited the restaurant.
amount is the total paid by a customer.

You are the restaurant owner and you want to analyze a possible expansion (there will be at least one customer every day).

Compute the moving average of how much the customer paid in a seven days window (i.e., current day + 6 days before). average_amount should be rounded to two decimal places.

Return the result table ordered by visited_on in ascending order.

The result format is in the following example.

Example 1:

Input:
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
Output:
+--------------+--------------+----------------+
| visited_on   | amount       | average_amount |
+--------------+--------------+----------------+
| 2019-01-07   | 860          | 122.86         |
| 2019-01-08   | 840          | 120            |
| 2019-01-09   | 840          | 120            |
| 2019-01-10   | 1000         | 142.86         |
+--------------+--------------+----------------+
Explanation:
1st moving average from 2019-01-01 to 2019-01-07 has an average_amount of (100 + 110 + 120 + 130 + 110 + 140 + 150)/7 = 122.86
2nd moving average from 2019-01-02 to 2019-01-08 has an average_amount of (110 + 120 + 130 + 110 + 140 + 150 + 80)/7 = 120
3rd moving average from 2019-01-03 to 2019-01-09 has an average_amount of (120 + 130 + 110 + 140 + 150 + 80 + 110)/7 = 120
4th moving average from 2019-01-04 to 2019-01-10 has an average_amount of (130 + 110 + 140 + 150 + 80 + 110 + 130 + 150)/7 = 142.86

*/

# V0
# IDEA : DAILY TOTALS, THEN A SELF JOIN OVER THE 7-DAY RANGE
#
#   A day can have several customers, so first collapse to one row per day.
#   Then join each day (cur) to every day in [cur - 6, cur] (prev) and sum.
#   Only days with a full 7-day window are reported: the first is the
#   earliest date + 6. The average divides by 7 days, not by row count,
#   because the window is 7 calendar days (one customer every day is
#   guaranteed, so all 7 are present).
#
# time = O(d * 7 + n), space = O(d)    -- n = rows in Customer, d = distinct days; each day joins 7 others
WITH daily AS (
    SELECT visited_on, SUM(amount) AS amount
    FROM Customer
    GROUP BY visited_on
)
SELECT
    cur.visited_on,
    SUM(prev.amount)               AS amount,
    ROUND(SUM(prev.amount) / 7, 2) AS average_amount
FROM daily cur
JOIN daily prev
  ON DATEDIFF(cur.visited_on, prev.visited_on) BETWEEN 0 AND 6   -- prev is cur or up to 6 days before
WHERE DATEDIFF(cur.visited_on, (SELECT MIN(visited_on) FROM daily)) >= 6   -- a full window exists
GROUP BY cur.visited_on
ORDER BY cur.visited_on;



# V0-1
# IDEA: WINDOW FUNC (gpt)
WITH daily AS (
    SELECT
        visited_on,
        SUM(amount) AS daily_amount
    FROM Customer
    GROUP BY visited_on
),

sub AS (
    SELECT
        visited_on,
        SUM(daily_amount) OVER (
            ORDER BY visited_on
            ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
        ) AS amount,
        ROUND(
            AVG(daily_amount) OVER (
                ORDER BY visited_on
                ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
            ),
            2
        ) AS average_amount,
        COUNT(*) OVER (
            ORDER BY visited_on
            ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
        ) AS day_count
    FROM daily
)

SELECT
    visited_on,
    amount,
    average_amount
FROM sub
WHERE day_count = 7
ORDER BY visited_on;


# V0-2
# IDEA: WINDOW FUNC (gemini)
WITH DailyCustomer AS (
    SELECT
        visited_on,
        SUM(amount) AS daily_amount
    FROM Customer
    GROUP BY visited_on
),
RollingStats AS (
    SELECT
        visited_on,
        SUM(daily_amount) OVER (
            ORDER BY visited_on ASC
            ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
        ) AS amount,
        ROUND(
            AVG(daily_amount) OVER (
                ORDER BY visited_on ASC
                ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
            ),
            2
        ) AS average_amount,
        ROW_NUMBER() OVER (ORDER BY visited_on ASC) AS day_num
    FROM DailyCustomer
)
SELECT
    visited_on,
    amount,
    average_amount
FROM RollingStats
WHERE day_num >= 7
ORDER BY visited_on ASC;



# V1
# IDEA : WINDOW SUM OVER THE LAST 7 ROWS
#
#   Same daily totals, then SUM(...) OVER (ROWS BETWEEN 6 PRECEDING AND
#   CURRENT ROW). Rows equal days only because a customer comes every day;
#   ROW_NUMBER drops the first 6 days, whose windows are short.
#
# time = O(n + d log d), space = O(d)    -- n = rows in Customer, d = distinct days; the window sorts d rows
WITH daily AS (
    SELECT visited_on, SUM(amount) AS amount
    FROM Customer
    GROUP BY visited_on
),
windowed AS (
    SELECT
        visited_on,
        SUM(amount) OVER (ORDER BY visited_on
                          ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS amount,
        ROW_NUMBER() OVER (ORDER BY visited_on)                     AS day_no
    FROM daily
)
SELECT
    visited_on,
    amount,
    ROUND(amount / 7, 2) AS average_amount
FROM windowed
WHERE day_no >= 7          -- the first 6 days do not have a full window
ORDER BY visited_on;

# V1-1
# IDEA : WINDOW SUM OVER A 7-DAY RANGE (MYSQL ONLY)
#
#   RANGE BETWEEN INTERVAL 6 DAY PRECEDING frames by date value, not by row,
#   so it stays correct even if a day were missing. It is MySQL syntax that
#   SQLite cannot parse, so the smoke test reports it as SKIP.
#
# time = O(n + d log d), space = O(d)    -- n = rows in Customer, d = distinct days
WITH daily AS (
    SELECT visited_on, SUM(amount) AS amount
    FROM Customer
    GROUP BY visited_on
),
windowed AS (
    SELECT
        visited_on,
        SUM(amount) OVER (ORDER BY visited_on
                          RANGE BETWEEN INTERVAL 6 DAY PRECEDING AND CURRENT ROW) AS amount
    FROM daily
)
SELECT
    visited_on,
    amount,
    ROUND(amount / 7, 2) AS average_amount
FROM windowed
WHERE visited_on >= (SELECT DATE_ADD(MIN(visited_on), INTERVAL 6 DAY) FROM daily)
ORDER BY visited_on;
