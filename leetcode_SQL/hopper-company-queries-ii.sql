/*

Hopper Company Queries II Problem

Description
LeetCode Problem 1645.

Table: Drivers

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| driver_id   | int     |
| join_date   | date    |
+-------------+---------+
driver_id is the primary key for this table.
Each row of this table contains the driver's ID and the date they joined the Hopper company.
Table: Rides

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| ride_id      | int     |
| user_id      | int     |
| requested_at | date    |
+--------------+---------+
ride_id is the primary key for this table.
Each row of this table contains the ID of a ride, the user's ID that requested it, and the day they requested it.
There may be some ride requests in this table that were not accepted.
Table: AcceptedRides

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| ride_id       | int     |
| driver_id     | int     |
| ride_distance | int     |
| ride_duration | int     |
+---------------+---------+
ride_id is the primary key for this table.
Each row of this table contains some information about an accepted ride.
It is guaranteed that each accepted ride exists in the Rides table.
Write an SQL query to report the percentage of working drivers (working_percentage) for each month of 2020 where:

percentage_month = (# drivers that accepted at least one ride during the month) / (# available drivers during the month) * 100.

Note that if the number of available drivers during a month is zero, we consider the working_percentage to be 0.

Return the result table ordered by month in ascending order, where month is the month’s number (January is 1, February is 2, etc.). Round working_percentage to the nearest 2 decimal places.

The query result format is in the following example.

Drivers table:
+-----------+------------+
| driver_id | join_date  |
+-----------+------------+
| 10        | 2019-12-10 |
| 8         | 2020-1-13  |
| 5         | 2020-2-16  |
| 7         | 2020-3-8   |
| 4         | 2020-5-17  |
| 1         | 2020-10-24 |
| 6         | 2021-1-5   |
+-----------+------------+

Rides table:
+---------+---------+--------------+
| ride_id | user_id | requested_at |
+---------+---------+--------------+
| 6       | 75      | 2019-12-9    |
| 1       | 54      | 2020-2-9     |
| 10      | 63      | 2020-3-4     |
| 19      | 39      | 2020-4-6     |
| 3       | 41      | 2020-6-3     |
| 13      | 52      | 2020-6-22    |
| 7       | 69      | 2020-7-16    |
| 17      | 70      | 2020-8-25    |
| 20      | 81      | 2020-11-2    |
| 5       | 57      | 2020-11-9    |
| 2       | 42      | 2020-12-9    |
| 11      | 68      | 2021-1-11    |
| 15      | 32      | 2021-1-17    |
| 12      | 11      | 2021-1-19    |
| 14      | 18      | 2021-1-27    |
+---------+---------+--------------+

AcceptedRides table:
+---------+-----------+---------------+---------------+
| ride_id | driver_id | ride_distance | ride_duration |
+---------+-----------+---------------+---------------+
| 10      | 10        | 63            | 38            |
| 13      | 10        | 73            | 96            |
| 7       | 8         | 100           | 28            |
| 17      | 7         | 119           | 68            |
| 20      | 1         | 121           | 92            |
| 5       | 7         | 42            | 101           |
| 2       | 4         | 6             | 38            |
| 11      | 8         | 37            | 43            |
| 15      | 8         | 108           | 82            |
| 12      | 8         | 38            | 34            |
| 14      | 1         | 90            | 74            |
+---------+-----------+---------------+---------------+

Result table:
+-------+--------------------+
| month | working_percentage |
+-------+--------------------+
| 1     | 0.00               |
| 2     | 0.00               |
| 3     | 25.00              |
| 4     | 0.00               |
| 5     | 0.00               |
| 6     | 20.00              |
| 7     | 20.00              |
| 8     | 20.00              |
| 9     | 0.00               |
| 10    | 0.00               |
| 11    | 33.33              |
| 12    | 16.67              |
+-------+--------------------+

By the end of January --> two active drivers (10, 8) and no accepted rides. The percentage is 0%.
By the end of February --> three active drivers (10, 8, 5) and no accepted rides. The percentage is 0%.
By the end of March --> four active drivers (10, 8, 5, 7) and one accepted ride by driver (10). The percentage is (1 / 4) * 100 = 25%.
By the end of April --> four active drivers (10, 8, 5, 7) and no accepted rides. The percentage is 0%.
By the end of May --> five active drivers (10, 8, 5, 7, 4) and no accepted rides. The percentage is 0%.
By the end of June --> five active drivers (10, 8, 5, 7, 4) and one accepted ride by driver (10). The percentage is (1 / 5) * 100 = 20%.
By the end of July --> five active drivers (10, 8, 5, 7, 4) and one accepted ride by driver (8). The percentage is (1 / 5) * 100 = 20%.
By the end of August --> five active drivers (10, 8, 5, 7, 4) and one accepted ride by driver (7). The percentage is (1 / 5) * 100 = 20%.
By the end of Septemeber --> five active drivers (10, 8, 5, 7, 4) and no accepted rides. The percentage is 0%.
By the end of October --> six active drivers (10, 8, 5, 7, 4, 1) and no accepted rides. The percentage is 0%.
By the end of November --> six active drivers (10, 8, 5, 7, 4, 1) and two accepted rides by two different drivers (1, 7). The percentage is (2 / 6) * 100 = 33.33%.
By the end of December --> six active drivers (10, 8, 5, 7, 4, 1) and one accepted ride by driver (4). The percentage is (1 / 6) * 100 = 16.67%.

*/


# V0

# V1
# https://circlecoder.com/hopper-company-queries-II/
select months_drivers.month, round(coalesce(100 * coalesce(total_active_drivers, 0) / total_drivers, 0), 2) as working_percentage
from
(
    select month, count(driver_id) as total_drivers
    from Drivers as a
    right join
    (
        select "2020-1-31" as day, 1 as month
        union select "2020-2-29", 2
        union select "2020-3-31", 3
        union select "2020-4-30", 4
        union select "2020-5-31", 5
        union select "2020-6-30", 6
        union select "2020-7-31", 7
        union select "2020-8-31", 8
        union select "2020-9-30", 9
        union select "2020-10-31", 10
        union select "2020-11-30", 11
        union select "2020-12-31", 12
    ) as months
    on join_date <= day
    group by month
) months_drivers
left join
(
    select month, count(distinct b.driver_id) as total_active_drivers
    from
    (
        select ride_id, cast(substr(requested_at, 6, 2) as unsigned) as month
        from Rides
        where substr(requested_at, 1, 4) = "2020"
    ) month_rides
    join AcceptedRides as b
    on month_rides.ride_id = b.ride_id
    group by month
) months_active_drivers
on months_drivers.month = months_active_drivers.month;

# V2
# Time:  O(d + r + tlogt)
# Space: O(d + r + t)
WITH RECURSIVE year_cte AS (
    SELECT MIN(YEAR(join_date)) year,
           MAX(YEAR(join_date)) max_year
    FROM drivers
    UNION ALL
    SELECT year + 1,
           max_year
    FROM year_cte
    WHERE year < max_year
),
year_month_cte AS (
    SELECT year,
           1 month,
           12 max_month
    FROM year_cte
    UNION ALL
    SELECT year,
           month + 1,
           max_month
    FROM year_month_cte
    WHERE month < max_month
),
drivers_count_cte AS(
    SELECT MAX(YEAR(d.join_date)) 'year',
           MAX(MONTH(d.join_date)) 'month',
           COUNT(d.driver_id) drivers_count
    FROM drivers d
    GROUP BY LEFT(d.join_date, 7)
),
working_drivers_count_cte AS (
    SELECT MAX(YEAR(r.requested_at)) 'year',
           MAX(MONTH(r.requested_at)) 'month',
           COUNT(DISTINCT a.driver_id) working_drivers
    FROM rides r LEFT JOIN acceptedrides a ON r.ride_id = a.ride_id
    GROUP BY LEFT(r.requested_at, 7)    
)

SELECT month,
       ROUND(IFNULL(working_drivers/active_drivers*100, 0), 2) AS working_percentage
FROM (
    SELECT t1.month,
           t1.year,
           IFNULL(SUM(t2.drivers_count) OVER (ORDER BY t1.year, t1.month), 0) active_drivers, 
           IFNULL(t3.working_drivers, 0) working_drivers
    FROM year_month_cte t1
         LEFT JOIN drivers_count_cte t2         ON t1.year = t2.year AND t1.month = t2.month 
         LEFT JOIN working_drivers_count_cte t3 ON t1.year = t3.year AND t1.month = t3.month
) tmp
WHERE year = 2020;