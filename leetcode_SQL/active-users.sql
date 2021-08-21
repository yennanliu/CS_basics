/*

Table Accounts:

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| name          | varchar |
+---------------+---------+
the id is the primary key for this table.
This table contains the account id and the user name of each account.
Table Logins:

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| login_date    | date    |
+---------------+---------+
There is no primary key for this table, it may contain duplicates.
This table contains the account id of the user who logged in and the login date. A user may log in multiple times in the day.
Write an SQL query to find the id and the name of active users.

Active users are those who logged in to their accounts for 5 or more consecutive days.

Return the result table ordered by the id.

The query result format is in the following example:

Accounts table:
+----+----------+
| id | name     |
+----+----------+
| 1  | Winston  |
| 7  | Jonathan |
+----+----------+

Logins table:
+----+------------+
| id | login_date |
+----+------------+
| 7  | 2020-05-30 |
| 1  | 2020-05-30 |
| 7  | 2020-05-31 |
| 7  | 2020-06-01 |
| 7  | 2020-06-02 |
| 7  | 2020-06-02 |
| 7  | 2020-06-03 |
| 1  | 2020-06-07 |
| 7  | 2020-06-10 |
+----+------------+

Result table:
+----+----------+
| id | name     |
+----+----------+
| 7  | Jonathan |
+----+----------+
User Winston with id = 1 logged in 2 times only in 2 different days, so, Winston is not an active user.
User Jonathan with id = 7 logged in 7 times in 6 different days, five of them were consecutive days, so, Jonathan is an active user.
Follow up question:
Can you write a general solution if the active users are those who logged in to their accounts for n or more consecutive days?


*/


# V0 -> DOUBLE CHECK IF CORRECT
WITH cte AS
  (SELECT id,
          login_date,
          ROW_NUMBER() OVER (PARTITION BY id
                             ORDER BY login_date) AS row_num
   FROM Logins)
SELECT c.DISTINCT id
FROM cte c
INNER JOIN Accounts a ON a.id = cte.id
WHERE c.row_num >= 5

# V1
# https://zhuanlan.zhihu.com/p/264789993
WITH
tmp AS(
SELECT a.id, a.name, b.login_date,
ROW_NUMBER() OVER(PARTITION BY a.id ORDER BY b.login_date) AS rnk, 
DATEDIFF(b.login_date, '2020-01-01') AS diff 
FROM Accounts AS a
LEFT JOIN (
SELECT DISTINCT id, login_date FROM Logins
) AS b
ON a.id = b.id
)
SELECT DISTINCT id, name FROM tmp
GROUP BY id, name, diff-rnk
HAVING COUNT(login_date) >= 5;

# V1'
# https://code.dennyzhang.com/active-users
select *
from Accounts
where id in
    (select distinct t1.id
    from Logins as t1 inner join Logins as t2
    on t1.id = t2.id and datediff(t1.login_date, t2.login_date) between 1 and 4
    group by t1.id, t1.login_date
    having count(distinct(t2.login_date)) = 4)
order by id

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT DISTINCT 
       r.id,
       r.name
FROM
  (SELECT a_l.id,
          a_l.name,
          @accu := CASE
                       WHEN a_l.name = @prev AND 
                            DATEDIFF(a_l.login_date, @login_date) = 1
                       THEN @accu + 1
                       ELSE 1
                   END AS accu,
          @prev := a_l.name AS prev,
          @login_date := a_l.login_date AS login_date
   FROM (
           (SELECT DISTINCT
                   a.id,
                   a.name,
                   l.login_date
            FROM accounts a
            LEFT JOIN logins l
            ON a.id = l.id
            ORDER BY a.id,
                     a.name,
                     l.login_date) a_l,
           (SELECT @accu := 0,
                   @prev := "",
                   @login_date := "") init
        )
  ) r
WHERE r.accu = 5;