/*

https://leetcode.com/problems/customers-who-never-order/description/

# Time:  O(n^2)
# Space: O(1)
#
# Suppose that a website contains two tables, the Customers table and the Orders table. Write a SQL query to find all customers who never order anything.
# 
# Table: Customers.
# 
# +----+-------+
# | Id | Name  |
# +----+-------+
# | 1  | Joe   |
# | 2  | Henry |
# | 3  | Sam   |
# | 4  | Max   |
# +----+-------+
# Table: Orders.
# 
# +----+------------+
# | Id | CustomerId |
# +----+------------+
# | 1  | 3          |
# | 2  | 1          |
# +----+------------+
# Using the above tables as example, return the following:
# 
# +-----------+
# | Customers |
# +-----------+
# | Henry     |
# | Max       |
# +-----------+
#
*/

/* V0 */
### NOTE : 
#       -> SHOULD BE o.CustomerId is NULL
#       -> RATHER THAN o.CustomerId = NULL
SELECT
c.Name AS Customers
FROM
Customers c
left join
Orders o
on c.Id = o.CustomerId
WHERE
o.CustomerId is NULL

/* V1 */
SELECT Name AS Customers
FROM Customers
LEFT JOIN Orders ON Customers.Id = Orders.CustomerId
WHERE Orders.CustomerId IS NULL

/* V2 */
SELECT Name AS Customers FROM Customers WHERE Id NOT IN (SELECT CustomerId FROM Orders)
