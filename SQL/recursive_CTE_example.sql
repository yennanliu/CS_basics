# Recursive sql example via CTE 
# http://diary.tw/tim/archive/20130723#recentTrackback

WITH cte (num, mysum) AS (  
  SELECT 1 as num, 1 as mysum  
  UNION ALL  
  SELECT num + 1, mysum + num + 1 FROM cte a where num <10  
)  
SELECT * FROM cte 