/*

Table: Customers

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| customer_id   | int     |
| customer_name | varchar |
| email         | varchar |
+---------------+---------+
customer_id is the primary key for this table.
Each row of this table contains the name and the email of a customer of an online shop.
Table: Contacts

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user_id       | id      |
| contact_name  | varchar |
| contact_email | varchar |
+---------------+---------+
(user_id, contact_email) is the primary key for this table.
Each row of this table contains the name and email of one contact of customer with user_id.
This table contains information about people each customer trust. The contact may or may not exist in the Customers table.
Table: Invoices

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| invoice_id   | int     |
| price        | int     |
| user_id      | int     |
+--------------+---------+
invoice_id is the primary key for this table.
Each row of this table indicates that user_id has an invoice with invoice_id and a price.
Write an SQL query to find the following for each invoice_id:

customer_name: The name of the customer the invoice is related to.
price: The price of the invoice.
contacts_cnt: The number of contacts related to the customer.
trusted_contacts_cnt: The number of contacts related to the customer and at the same time they are customers to the shop. (i.e His/Her email exists in the Customers table.)
Order the result table by invoice_id.

The query result format is in the following example:

Customers table:
+-------------+---------------+--------------------+
| customer_id | customer_name | email              |
+-------------+---------------+--------------------+
| 1           | Alice         | alice@leetcode.com |
| 2           | Bob           | bob@leetcode.com   |
| 13          | John          | john@leetcode.com  |
| 6           | Alex          | alex@leetcode.com  |
+-------------+---------------+--------------------+
Contacts table:
+-------------+--------------+--------------------+
| user_id     | contact_name | contact_email      |
+-------------+--------------+--------------------+
| 1           | Bob          | bob@leetcode.com   |
| 1           | John         | john@leetcode.com  |
| 1           | Jal          | jal@leetcode.com   |
| 2           | Omar         | omar@leetcode.com  |
| 2           | Meir         | meir@leetcode.com  |
| 6           | Alice        | alice@leetcode.com |
+-------------+--------------+--------------------+
Invoices table:
+------------+-------+---------+
| invoice_id | price | user_id |
+------------+-------+---------+
| 77         | 100   | 1       |
| 88         | 200   | 1       |
| 99         | 300   | 2       |
| 66         | 400   | 2       |
| 55         | 500   | 13      |
| 44         | 60    | 6       |
+------------+-------+---------+
Result table:
+------------+---------------+-------+--------------+----------------------+
| invoice_id | customer_name | price | contacts_cnt | trusted_contacts_cnt |
+------------+---------------+-------+--------------+----------------------+
| 44         | Alex          | 60    | 1            | 1                    |
| 55         | John          | 500   | 0            | 0                    |
| 66         | Bob           | 400   | 2            | 0                    |
| 77         | Alice         | 100   | 3            | 2                    |
| 88         | Alice         | 200   | 3            | 2                    |
| 99         | Bob           | 300   | 2            | 0                    |
+------------+---------------+-------+--------------+----------------------+
Alice has three contacts, two of them are trusted contacts (Bob and John).
Bob has two contacts, none of them is a trusted contact.
Alex has one contact and it is a trusted contact (Alice).
John doesn't have any contacts.

*/

# V0
select invoice_id, customer_name, price, 
 count(Contacts.user_id) as contacts_cnt,
 sum(case when Contacts.contact_name in (select customer_name from Customers) then 1 else 0 end) as trusted_contacts_cnt
from Invoices inner join Customers on Invoices.user_id = Customers.customer_id
left join Contacts on Customers.customer_id = Contacts.user_id
group by Invoices.invoice_id, customer_name
order by Invoices.invoice_id

# V0'
-- SELECT
-- i1.invoice_id,
-- c2.customer_name,
-- i1.price,
-- COUNT(c2.user_id) AS contacts_cnt,
-- SUM(CASE WHEN c1.contact_name IS NOT NULL THEN 1 ELSE 0 END) AS  trusted_contacts_cnt
-- FROM
-- Customers c1
-- INNER JOIN
-- Invoices i1
-- ON
-- c1.user_id = i1.user_id
-- LEFT JOIN
-- Contacts c2 
-- ON
-- c1.customer_id = c2.user_id
-- GROUP BY
-- i1.invoice_id,
-- c2.customer_name

# V1
# https://code.dennyzhang.com/number-of-trusted-contacts-of-a-customer
select invoice_id, customer_name, price, 
 count(Contacts.user_id) as contacts_cnt,
 sum(case when Contacts.contact_name in (select customer_name from Customers) then 1 else 0 end) as trusted_contacts_cnt
from Invoices inner join Customers on Invoices.user_id = Customers.customer_id
left join Contacts on Customers.customer_id = Contacts.user_id
group by Invoices.invoice_id, customer_name
order by Invoices.invoice_id

# V2
# Time:  O(n + m + l + nlogn), n, m, l are the sizes of invoices, customers, contacts
# Space: O(n + m + l)
SELECT i.invoice_id, 
       c.customer_name, 
       i.price, 
       Ifnull(tmp.c_cnt, 0) AS contacts_cnt, 
       Ifnull(tmp.t_cnt, 0) AS trusted_contacts_cnt 
FROM   invoices i 
       LEFT JOIN (SELECT co.user_id, 
                         Count(co.user_id)       AS c_cnt, 
                         Count(cu.customer_name) AS t_cnt 
                  FROM   contacts co 
                         LEFT JOIN customers cu 
                                ON co.contact_name = cu.customer_name 
                                   AND co.contact_email = cu.email 
                  GROUP  BY co.user_id 
                  ORDER  BY NULL) tmp 
              ON i.user_id = tmp.user_id 
       LEFT JOIN customers c 
              ON i.user_id = c.customer_id 
ORDER  BY i.invoice_id;