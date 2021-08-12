-- 1126.Active Businesses
-- Table: Events

-- +---------------+---------+
-- | Column Name   | Type    |
-- +---------------+---------+
-- | business_id   | int     |
-- | event_type    | varchar |
-- | occurences    | int     | 
-- +---------------+---------+
-- (business_id, event_type) is the primary key of this table.
-- Each row in the table logs the info that an event of some type occured at some business for a number of times.
 

-- Write an SQL query to find all active businesses.

-- An active business is a business that has more than one event type with occurences greater than the average occurences of that event type among all businesses.

-- The query result format is in the following example:

-- Events table:
-- +-------------+------------+------------+
-- | business_id | event_type | occurences |
-- +-------------+------------+------------+
-- | 1           | reviews    | 7          |
-- | 3           | reviews    | 3          |
-- | 1           | ads        | 11         |
-- | 2           | ads        | 7          |
-- | 3           | ads        | 6          |
-- | 1           | page views | 3          |
-- | 2           | page views | 12         |
-- +-------------+------------+------------+

-- Result table:
-- +-------------+
-- | business_id |
-- +-------------+
-- | 1           |
-- +-------------+ 
-- Average for 'reviews', 'ads' and 'page views' are (7+3)/2=5, (11+7+6)/3=8, (3+12)/2=7.5 respectively.
-- Business with id 1 has 7 'reviews' events (more than 5) and 11 'ads' events (more than 8) so it is an active business.


# V0
### NOTE THE CONDITION IN t1, t2
select t1.business_id
from (
    select business_id, event_type, avg(occurences) as avgo
    from Events
    group by business_id, event_type) as t1 inner join 
    (select event_type, avg(occurences) as avgo
    from Events
    group by event_type) as t2
on t1.event_type = t2.event_type
where t1.avgo > t2.avgo
group by business_id
having count(1) > 1

# V1
# https://code.dennyzhang.com/active-businesses
select t1.business_id
from (
    select business_id, event_type, avg(occurences) as avgo
    from Events
    group by business_id, event_type) as t1 inner join 
    (select event_type, avg(occurences) as avgo
    from Events
    group by event_type) as t2
on t1.event_type = t2.event_type
where t1.avgo>t2.avgo
group by business_id
having count(1)>1

# V2
# Time:  O(n)
# Space: O(n)
SELECT business_id
FROM EVENTS
JOIN
  (SELECT event_type,
          avg(occurences) AS average
   FROM EVENTS
   GROUP BY event_type) AS TEMP ON Events.event_type = temp.event_type
AND Events.occurences > temp.average
GROUP BY business_id
HAVING count(DISTINCT Events.event_type) > 1
ORDER BY NULL