# V0

# V1
# https://blog.csdn.net/weixin_44171872/article/details/120108523
SELECT
DISTINCT c1.user_id As user_id
FROM
Confirmations AS c1
JOIN
Confirmations AS c2
ON
c1.user_id = c2.user_id
WHERE
c1.time_stamp < c2.time_stamp
AND
TIMESTAMPDIFF(SECOND, c1.time_stamp, c2.time_stamp) <  24*60*60
ORDER BY user_id

# V2
# Time:  O(nlogn)
# Space: O(n)
WITH confirmation_time_cte AS
  (SELECT user_id,
          TIMESTAMPDIFF(SECOND,
                        time_stamp,
                        LEAD(time_stamp, 1) OVER (PARTITION BY user_id
                                                  ORDER BY time_stamp)) AS time_diff
   FROM Confirmations)

SELECT DISTINCT user_id
FROM confirmation_time_cte
WHERE time_diff <= 86400;