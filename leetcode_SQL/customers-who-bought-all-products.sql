# V0 

# V1 
# https://code.dennyzhang.com/customers-who-bought-all-products
select customer_id
from Customer
group by customer_id
having count(distinct product_key) = (
    select count(1)
    from Product)

# V2 
# Time:  O(n + k), n is number of customer, k is number of product
# Space: O(n + k)
SELECT customer_id
FROM customer
GROUP BY customer_id
HAVING count(DISTINCT product_key)=
  (SELECT count(DISTINCT product_key)
   FROM product)
ORDER BY NULL