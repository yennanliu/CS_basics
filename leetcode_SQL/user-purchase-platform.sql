-- # 1127
-- Table: Spending

-- +-------------+---------+
-- | Column Name | Type    |
-- +-------------+---------+
-- | user_id     | int     |
-- | spend_date  | date    |
-- | platform    | enum    | 
-- | amount      | int     |
-- +-------------+---------+
-- The table logs the spendings history of users that make purchases from an online shopping website which has a desktop and a mobile application.
-- (user_id, spend_date, platform) is the primary key of this table.
-- The platform column is an ENUM type of ('desktop', 'mobile').
-- Write an SQL query to find the total number of users and the total amount spent using mobile only, desktop only and both mobile and desktop together for each date.

-- The query result format is in the following example:

-- Spending table:
-- +---------+------------+----------+--------+
-- | user_id | spend_date | platform | amount |
-- +---------+------------+----------+--------+
-- | 1       | 2019-07-01 | mobile   | 100    |
-- | 1       | 2019-07-01 | desktop  | 100    |
-- | 2       | 2019-07-01 | mobile   | 100    |
-- | 2       | 2019-07-02 | mobile   | 100    |
-- | 3       | 2019-07-01 | desktop  | 100    |
-- | 3       | 2019-07-02 | desktop  | 100    |
-- +---------+------------+----------+--------+

-- Result table:
-- +------------+----------+--------------+-------------+
-- | spend_date | platform | total_amount | total_users |
-- +------------+----------+--------------+-------------+
-- | 2019-07-01 | desktop  | 100          | 1           |
-- | 2019-07-01 | mobile   | 100          | 1           |
-- | 2019-07-01 | both     | 200          | 1           |
-- | 2019-07-02 | desktop  | 100          | 1           |
-- | 2019-07-02 | mobile   | 100          | 1           |
-- | 2019-07-02 | both     | 0            | 0           |
-- +------------+----------+--------------+-------------+ 
-- On 2019-07-01, user 1 purchased using both desktop and mobile, user 2 purchased using mobile only and user 3 purchased using desktop only.
-- On 2019-07-02, user 2 purchased using mobile only, user 3 purchased using desktop only and no one purchased using both platforms.

# V0

# V1
# https://zhuanlan.zhihu.com/p/260379510
### NOTE tmp CTE will get the count and its platform (mobile, destop, both)
WITH
tmp AS (
SELECT spend_date, user_id, SUM(amount) AS total_amount,
CASE WHEN COUNT(DISTINCT platform) = 2 THEN 'both'
ELSE platform END as platform
FROM Spending
GROUP BY spend_date, user_id
),

tmp1 AS (
SELECT  DISTINCT spend_date,'mobile' as platform
FROM Spending
UNION ALL
SELECT  DISTINCT spend_date,'both'
FROM Spending 
UNION ALL
SELECT  DISTINCT spend_date,'desktop'
FROM Spending
)

SELECT a.spend_date, a.platform, 
COALESCE(SUM(b.total_amount),0) AS total_amount, 
COALESCE(COUNT(DISTINCT b.user_id),0) AS total_users 
FROM tmp1 AS a
LEFT JOIN tmp AS b
ON a.spend_date = b.spend_date
AND a.platform = b.platform
GROUP BY spend_date, platform;

# V1'
# https://ladychili.top/leetcode/sql/1127.UserPurchasePlatform.html
SELECT aa.spend_date, 
       aa.platform, 
       COALESCE(bb.total_amount, 0) total_amount, 
       COALESCE(bb.total_users,0) total_users
FROM 
    (SELECT DISTINCT(spend_date), a.platform   -- table aa
    FROM Spending JOIN
        (SELECT 'desktop' AS platform UNION
        SELECT 'mobile' AS platform UNION
        SELECT 'both' AS platform
        ) a 
    ) aa
    LEFT JOIN 
    (SELECT spend_date,                      -- table bb
            platform, 
            SUM(amount) total_amount, 
            COUNT(user_id) total_users
    FROM
        (SELECT spend_date, 
                user_id, 
                (CASE COUNT(DISTINCT platform)
                    WHEN 1 THEN platform
                    WHEN 2 THEN 'both'
                    END) platform,
                SUM(amount) amount
        FROM Spending
        GROUP BY spend_date, user_id
        ) b
    GROUP BY spend_date, platform
    ) bb
    ON aa.platform = bb.platform AND 
    aa.spend_date = bb.spend_date

# V2
# Time:  O(n)
# Space: O(n)
SELECT t1.spend_date, 
       'both'                       AS platform, 
       Sum(Ifnull(t.sum_amount, 0)) AS total_amount, 
       Count(t.user_id)             AS total_users 
FROM   (SELECT spend_date, 
               user_id, 
               Sum(amount) AS sum_amount 
        FROM   spending 
        GROUP  BY spend_date, 
                  user_id 
        HAVING Count(platform) = 2) AS t 
       RIGHT JOIN (SELECT DISTINCT spend_date 
                   FROM   spending) AS t1
               ON t.spend_date = t1.spend_date 
GROUP  BY t1.spend_date 
UNION 
SELECT t2.spend_date, 
       'mobile'                 AS platform, 
       Sum(Ifnull(t.amount, 0)) AS total_amount, 
       Count(t.user_id)         AS total_users 
FROM   (SELECT spend_date, 
               user_id, 
               platform, 
               amount 
        FROM   spending 
        GROUP  BY spend_date, 
                  user_id 
        HAVING Count(platform) < 2) AS t 
       RIGHT JOIN (SELECT DISTINCT spend_date 
                   FROM   spending) AS t2 
               ON t.spend_date = t2.spend_date 
                  AND t.platform = 'mobile' 
GROUP  BY t2.spend_date 
UNION 
SELECT t3.spend_date, 
       'desktop'                AS platform, 
       Sum(Ifnull(t.amount, 0)) AS total_amount, 
       Count(t.user_id)         AS total_users 
FROM   (SELECT spend_date, 
               user_id, 
               platform, 
               amount 
        FROM   spending 
        GROUP  BY spend_date, 
                  user_id 
        HAVING Count(platform) < 2) AS t 
       RIGHT JOIN (SELECT DISTINCT spend_date 
                   FROM   spending) AS t3
               ON t.spend_date = t3.spend_date 
                  AND t.platform = 'desktop' 
GROUP  BY t3.spend_date