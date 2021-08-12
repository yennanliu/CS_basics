-- Table: Books

-- +----------------+---------+
-- | Column Name    | Type    |
-- +----------------+---------+
-- | book_id        | int     |
-- | name           | varchar |
-- | available_from | date    |
-- +----------------+---------+
-- book_id is the primary key of this table.
-- Table: Orders

-- +----------------+---------+
-- | Column Name    | Type    |
-- +----------------+---------+
-- | order_id       | int     |
-- | book_id        | int     |
-- | quantity       | int     |
-- | dispatch_date  | date    |
-- +----------------+---------+
-- order_id is the primary key of this table.
-- book_id is a foreign key to the Books table.
-- Write an SQL query that reports the books that have sold less than 10 copies in the last year, excluding books that have been available for less than 1 month from today. Assume today is 2019-06-23.

-- The query result format is in the following example:

-- Books table:
-- +---------+--------------------+----------------+
-- | book_id | name               | available_from |
-- +---------+--------------------+----------------+
-- | 1       | "Kalila And Demna" | 2010-01-01     |
-- | 2       | "28 Letters"       | 2012-05-12     |
-- | 3       | "The Hobbit"       | 2019-06-10     |
-- | 4       | "13 Reasons Why"   | 2019-06-01     |
-- | 5       | "The Hunger Games" | 2008-09-21     |
-- +---------+--------------------+----------------+

-- Orders table:
-- +----------+---------+----------+---------------+
-- | order_id | book_id | quantity | dispatch_date |
-- +----------+---------+----------+---------------+
-- | 1        | 1       | 2        | 2018-07-26    |
-- | 2        | 1       | 1        | 2018-11-05    |
-- | 3        | 3       | 8        | 2019-06-11    |
-- | 4        | 4       | 6        | 2019-06-05    |
-- | 5        | 4       | 5        | 2019-06-20    |
-- | 6        | 5       | 9        | 2009-02-02    |
-- | 7        | 5       | 8        | 2010-04-13    |
-- +----------+---------+----------+---------------+

-- Result table:
-- +-----------+--------------------+
-- | book_id   | name               |
-- +-----------+--------------------+
-- | 1         | "Kalila And Demna" |
-- | 2         | "28 Letters"       |
-- | 5         | "The Hunger Games" |
-- +-----------+--------------------+

# V0
### NOTE : the datetime condition (in where)
select book_id, name
from Books
where book_id not in (
    select book_id
    from Orders
    where dispatch_date >= '2018-06-23' and dispatch_date <= '2019-06-22'
    group by book_id
    having sum(quantity) >= 10)
and available_from < '2019-05-23'

# V1 
# https://code.dennyzhang.com/unpopular-books
select book_id, name
from Books
where book_id not in (
    select book_id
    from Orders
    where dispatch_date >= '2018-06-23' and dispatch_date <= '2019-06-22'
    group by book_id
    having sum(quantity) >= 10)
and available_from < '2019-05-23'

# V2 
# Time:  O(m + n)
# Space: O(n)
SELECT b.book_id, 
       b.NAME 
FROM   books AS b 
       LEFT JOIN orders AS o 
              ON b.book_id = o.book_id 
                 AND dispatch_date BETWEEN '2018-06-23' AND '2019-6-23' 
WHERE  Datediff('2019-06-23', b.available_from) > 30 
GROUP BY book_id 
HAVING Sum(IFNULL(o.quantity, 0)) < 10 ORDER  BY NULL 