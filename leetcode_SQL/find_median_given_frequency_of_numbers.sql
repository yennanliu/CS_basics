/*

Find Median Given Frequency Of Numbers Problem

Description
LeetCode Problem 571.

The Numbers table keeps the value of number and its frequency.

+----------+-------------+
|  Number  |  Frequency  |
+----------+-------------|
|  0       |  7          |
|  1       |  1          |
|  2       |  3          |
|  3       |  1          |
+----------+-------------+
In this table, the numbers are 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 3, so the median is (0 + 0) / 2 = 0.

+--------+
| median |
+--------|
| 0.0000 |
+--------+
Write a query to find the median of all numbers and name the result as median.


*/


# Ref : find median in SQL
# https://www.sisense.com/blog/medians-in-sql/
# The where clause ensures that we’ll get the two middle values if there is an even number of values, and the single middle number if there is an odd number of values because between is inclusive of its bounds.
-- select avg(price) as median
-- from ordered_purchases
-- where row_id between ct/2.0 and ct/2.0 + 1


# V0


# V1
# https://circlecoder.com/find-median-given-frequency-of-numbers/
select round(sum(Number) / count(Number), 2) as median
from (select Number, Frequency,
      sum(Frequency) over (order by Number) as cumulative_num,
      sum(Frequency) over () as total_num
      from Numbers) sub
where total_num / 2.0 between cumulative_num - Frequency and cumulative_num

# V1'
# https://medium.com/jen-li-chen-in-data-science/leetcode-sql-39641ea1705d
with recursive cte as
(
  select number, frequency from Numbers
  union all
  select number, frequency -1 from cte where frequency > 1
)
select avg(number) as median
from
(
  select number, rank() over(order by number, frequency) as ranka,
  rank() over(order by number desc, frequency desc) as rankd
  from cte order by 1
) x
where ranka = rankd or ranka = rankd + 1 or ranka = rankd - 1

# V1''
# https://blog.csdn.net/Hello_JavaScript/article/details/103332960

# V2