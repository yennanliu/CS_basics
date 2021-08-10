/*

Table: Visits

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user_id       | int     |
| visit_date    | date    |
+---------------+---------+
(user_id, visit_date) is the primary key for this table.
Each row of this table indicates that user_id has visited the bank in visit_date.
Table: Transactions

+------------------+---------+
| Column Name      | Type    |
+------------------+---------+
| user_id          | int     |
| transaction_date | date    |
| amount           | int     |
+------------------+---------+
There is no primary key for this table, it may contain duplicates.
Each row of this table indicates that user_id has done a transaction of amount in transaction_date.
It is guaranteed that the user has visited the bank in the transaction_date.(i.e The Visits table contains (user_id, transaction_date) in one row)
Write an SQL query to find how many users visited the bank and didn’t do any transactions, how many visited the bank and did one transaction and so on.

The result table will contain two columns transactions_count which is the number of transactions done in one visit and visits_count which is the corresponding number of users who did transactions_count in one visit to the bank. transactions_count should take all values from 0 to max(transactions_count) done by one or more users.

Order the result table by transactions_count.

The query result format is in the following example:

Visits table:
+---------+------------+
| user_id | visit_date |
+---------+------------+
| 1       | 2020-01-01 |
| 2       | 2020-01-02 |
| 12      | 2020-01-01 |
| 19      | 2020-01-03 |
| 1       | 2020-01-02 |
| 2       | 2020-01-03 |
| 1       | 2020-01-04 |
| 7       | 2020-01-11 |
| 9       | 2020-01-25 |
| 8       | 2020-01-28 |
+---------+------------+
Transactions table:
+---------+------------------+--------+
| user_id | transaction_date | amount |
+---------+------------------+--------+
| 1       | 2020-01-02       | 120    |
| 2       | 2020-01-03       | 22     |
| 7       | 2020-01-11       | 232    |
| 1       | 2020-01-04       | 7      |
| 9       | 2020-01-25       | 33     |
| 9       | 2020-01-25       | 66     |
| 8       | 2020-01-28       | 1      |
| 9       | 2020-01-25       | 99     |
+---------+------------------+--------+
Result table:
+--------------------+--------------+
| transactions_count | visits_count |
+--------------------+--------------+
| 0                  | 4            |
| 1                  | 5            |
| 2                  | 0            |
| 3                  | 1            |
+--------------------+--------------+
Users 1, 2, 12 and 19 visited the bank in 2020-01-01, 2020-01-02, 2020-01-01 and 2020-01-03 respectively, and didn't do any transactions.
So we have visits_count = 4 for transactions_count = 0.
Users 2, 7 and 8 visited the bank in 2020-01-03, 2020-01-11 and 2020-01-28 respectively, and did one transaction.
User 1 Also visited the bank in 2020-01-02 and 2020-01-04 and did one transaction each day.
So we have total visits_count = 5 for transactions_count = 1.
For transactions_count = 2 we don't have any users who visited the bank and did two transactions.
For transactions_count = 3 we have user 9 who visited the bank in 2020-01-25 and did three transactions.
Note that we stopped at transactions_count = 3 as this is the maximum number of transactions done by one user in one visit to the bank.

*/

# V0

# V1
# https://code.dennyzhang.com/number-of-transactions-per-visit
select *
from (select cast(t4.id as SIGNED) transactions_count, 
    case when t1.visits_count is null then 0 else t1.visits_count end as visits_count
from (select t.transactions_count, count(1) as visits_count
    from (
        select sum(case when transaction_date is null then 0 else 1 end) as transactions_count, count(1) as visits_count
        from Visits left join Transactions
        on Visits.user_id = Transactions.user_id
        and visit_date = transaction_date
        group by Visits.user_id, Visits.visit_date) as t
    group by t.transactions_count) as t1
        right join
        (select (@cnt1 := @cnt1+1) as id
        from Transactions cross join (select @cnt1 := -1) as dummy
         union select 0
            ) as t4
on t1.transactions_count = t4.id) as t5
where t5.transactions_count <= 
    (select 0 as cnt
     union 
     select count(1) as cnt
    from Transactions
    group by user_id, transaction_date
    order by cnt desc
    limit 1)

# V2
# Time:  O(m + n)  
# Space: O(m + n)  
SELECT seq.transactions_count AS transactions_count,
       Ifnull(v.visits_count, 0) AS visits_count 
FROM   (SELECT @count := CAST(@count + 1 AS SIGNED) AS transactions_count 
        FROM   (SELECT @count := -1, @max_count := Ifnull(Max(count), 0)
                FROM   (SELECT Count(1) AS count 
                        FROM   transactions 
                        GROUP  BY user_id, 
                                  transaction_date 
                        ORDER  BY NULL) AS tmp) AS c
               CROSS JOIN (SELECT user_id 
                           FROM   visits 
                           UNION ALL 
                           SELECT user_id 
                           FROM   transactions) AS m_n
        WHERE  @count < @max_count
       ) AS seq
       LEFT JOIN (SELECT transactions_count, 
                         Count(1) AS visits_Count 
                  FROM   (SELECT Count(transaction_date) AS transactions_count 
                          FROM   visits AS v 
                                 LEFT JOIN transactions AS t 
                                        ON v.user_id = t.user_id 
                                           AND visit_date = transaction_date 
                          GROUP  BY v.user_id, 
                                    visit_date 
                          ORDER  BY NULL) AS visits_count 
                  GROUP  BY transactions_count) AS v 
              ON seq.transactions_count = v.transactions_count 