# V0 

# V1 
# https://code.dennyzhang.com/sales-analysis-i
select seller_id
from Sales
group by seller_id
having sum(price) = (
    select sum(price)
    from Sales
    group by seller_id
    order by sum(price) desc
    limit 1)

# V2 
# Time:  O(n)
# Space: O(n)
SELECT seller_id 
FROM   sales 
GROUP  BY seller_id 
HAVING Sum(price) = (SELECT Sum(price) 
                     FROM   sales 
                     GROUP  BY seller_id 
                     ORDER  BY Sum(price) DESC 
                     LIMIT  1) 
ORDER  BY NULL
