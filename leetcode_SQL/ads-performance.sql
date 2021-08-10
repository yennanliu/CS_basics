/*

Table: Ads

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| ad_id         | int     |
| user_id       | int     |
| action        | enum    |
+---------------+---------+
(ad_id, user_id) is the primary key for this table.
Each row of this table contains the ID of an Ad, the ID of a user and the action taken by this user regarding this Ad.
The action column is an ENUM type of ('Clicked', 'Viewed', 'Ignored').
A company is running Ads and wants to calculate the performance of each Ad.

Performance of the Ad is measured using Click-Through Rate (CTR) where:

Leetcode: Ads Performance

Write an SQL query to find the ctr of each Ad.

Round ctr to 2 decimal points. Order the result table by ctr in descending order and by ad_id in ascending order in case of a tie.

The query result format is in the following example:

Ads table:
+-------+---------+---------+
| ad_id | user_id | action  |
+-------+---------+---------+
| 1     | 1       | Clicked |
| 2     | 2       | Clicked |
| 3     | 3       | Viewed  |
| 5     | 5       | Ignored |
| 1     | 7       | Ignored |
| 2     | 7       | Viewed  |
| 3     | 5       | Clicked |
| 1     | 4       | Viewed  |
| 2     | 11      | Viewed  |
| 1     | 2       | Clicked |
+-------+---------+---------+
Result table:
+-------+-------+
| ad_id | ctr   |
+-------+-------+
| 1     | 66.67 |
| 3     | 50.00 |
| 2     | 33.33 |
| 5     | 0.00  |
+-------+-------+
for ad_id = 1, ctr = (2/(2+1)) * 100 = 66.67
for ad_id = 2, ctr = (1/(1+2)) * 100 = 33.33
for ad_id = 3, ctr = (1/(1+1)) * 100 = 50.00
for ad_id = 5, ctr = 0.00, Note that ad_id has no clicks or views.
Note that we don't care about Ignored Ads.
Result table is ordered by the ctr. in case of a tie we order them by ad_id

*/

# V0
SELECT ad_id,
       CASE
           WHEN clicks + views = 0 THEN 0
           ELSE ROUND(100 * clicks / (clicks + views), 2)
       END ctr
FROM
  (SELECT ad_id,
          SUM(CASE
                  WHEN action ='Viewed' THEN 1
                  ELSE 0
              END) views,
          SUM(CASE
                  WHEN action = 'Clicked' THEN 1
                  ELSE 0
              END) clicks
   FROM Ads
   GROUP BY ad_id) a
ORDER BY ctr DESC,
         ad_id ASC

# V0' : NEED TO FIX
-- WITH _ad_id AS (
-- SELECT
-- DISTINCT
-- ad_id
-- FROM 
-- Ads
-- )
-- SELECT
-- a.ad_id,
-- NULLIF(SUM(CASE WHEN action = 'Clicked' THEN ad_id ELSE 0 END) / SUM(CASE WHEN action IN ('Clicked', 'Viewed') THEN ad_id ELSE 0 END) , 0) AS ctr
-- FROM
-- Ads
-- RIGHT JOIN _ad_id
-- ON 
-- Ads.ad_id = _ad_id.ad_id
-- GROUP BY ad_id

# V1
# https://code.dennyzhang.com/ads-performance
select ad_id,
    (case when clicks+views = 0 then 0 else round(clicks/(clicks+views)*100, 2) end) as ctr
from 
    (select ad_id,
        sum(case when action='Clicked' then 1 else 0 end) as clicks,
        sum(case when action='Viewed' then 1 else 0 end) as views
    from Ads
    group by ad_id) as t
order by ctr desc, ad_id asc

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT ad_id,
       CASE
           WHEN clicks + views = 0 THEN 0
           ELSE ROUND(100 * clicks / (clicks + views), 2)
       END ctr
FROM
  (SELECT ad_id,
          SUM(CASE
                  WHEN action ='Viewed' THEN 1
                  ELSE 0
              END) views,
          SUM(CASE
                  WHEN action = 'Clicked' THEN 1
                  ELSE 0
              END) clicks
   FROM Ads
   GROUP BY ad_id) a
ORDER BY ctr DESC,
         ad_id ASC