# LeetCode SQL FAQ

> **Scope** — the tricks that LeetCode's SQL (Database) problems turn on, in MySQL 8: ranking and Top-N, duplicates, NULLs and missing rows, conditional aggregation and rates, pivots, comparing neighbouring rows, gaps and islands, running and moving windows, `UPDATE` / `DELETE` problems, and the MySQL details that fail a submission. Each trick is shown on the LeetCode problems that ask for it. Warehouse-scale SQL and query plans are not here.
> **See also**: [`../de/sql_for_data.md`](../de/sql_for_data.md) — the same patterns in PostgreSQL, with joins, NULL semantics and window frames explained from first principles; [`../de/sql_interview_questions.md`](../de/sql_interview_questions.md) — the senior data-engineering SQL round, where every answer gets an "and at 1 TB?" follow-up; [`./faq_sql.md`](./faq_sql.md) — the older, longer SQL notes.

Every query is **MySQL 8**, the dialect LeetCode grades SQL in, and every result shown is what
MySQL 8.0 returns on the data shown. Table and column names are LeetCode's own, so a query can
be pasted into the problem it names. Each section leads with the sentence to say out loud, then
the query, then the traps.

---

## 0) Before Writing the Query ⭐⭐⭐⭐⭐

Most wrong submissions are a misread question, not wrong syntax. Answer these five before
typing:

| Ask | Why it matters | Where it bites |
|---|---|---|
| **What is one output row?** (the grain) | decides the `GROUP BY` and whether a join can multiply rows | [1-3](#1-3-top-n-per-group), [4-2](#4-2-rates-and-percentages) |
| **What happens on a tie?** | picks `ROW_NUMBER`, `RANK` or `DENSE_RANK` | [1-1](#1-1-row_number-vs-rank-vs-dense_rank), [1-4](#1-4-first-or-latest-row-per-group) |
| **Can a value be `NULL`?** | `=`, `<>` and `NOT IN` silently drop `NULL` rows | [3-1](#3-1--and--drop-null-rows), [3-2](#3-2-never-did-x-the-anti-join) |
| **Must a missing row still appear?** | "report 0" or "return `null`" needs a `LEFT JOIN` or a scalar subquery | [1-2](#1-2-the-n-th-highest-value-null-when-there-is-none), [3-3](#3-3-keep-the-rows-with-zero-matches) |
| **Rounding and order?** | `ROUND(x, 2)` and "return in any order" vs an explicit `ORDER BY` | [4-2](#4-2-rates-and-percentages) |

---

## 1) Ranking and Top-N ⭐⭐⭐⭐⭐

### 1-1) `ROW_NUMBER` vs `RANK` vs `DENSE_RANK`

**They differ only on ties. `ROW_NUMBER` gives every row its own number, `RANK` gives tied rows
the same rank and skips the ranks after them, `DENSE_RANK` gives tied rows the same rank and
skips nothing.**

```sql
-- Bob and Charlie tie on 8000
SELECT name, salary,
       ROW_NUMBER() OVER (ORDER BY salary DESC) AS rn,
       RANK()       OVER (ORDER BY salary DESC) AS rnk,
       DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rnk
FROM   Employee
WHERE  departmentId = 1;
```

```text
name      salary   rn   rnk   dense_rnk
Amy        9000     1    1        1
Bob        8000     2    2        2
Charlie    8000     3    2        2      <- which of Bob / Charlie gets 2 is not guaranteed
David      7000     4    4        3
```

| Function | On a tie | Remember it as | Use it for |
|---|---|---|---|
| `ROW_NUMBER()` | every row still gets a unique number | one number per row | exactly one row per group, dedupe |
| `RANK()` | same rank, then a gap (1, 2, 2, 4) | sports ranking: a tie skips places | "first N places", ties kept |
| `DENSE_RANK()` | same rank, no gap (1, 2, 2, 3) | sports ranking with no skipped places | "N-th highest **distinct** value" |

LC 178 (*Rank Scores*) is `DENSE_RANK` by name — and it trips on a MySQL detail:

```sql
-- LC 178: `rank` is a reserved word in MySQL 8, so the alias needs backticks
SELECT score,
       DENSE_RANK() OVER (ORDER BY score DESC) AS `rank`
FROM   Scores
ORDER  BY score DESC;
```

- **Which tied row gets the lower `ROW_NUMBER` is undefined** unless `ORDER BY` decides it;
  Bob could be 3 on the next run. Add a unique column as a tie-breaker
  (`ORDER BY salary DESC, id`) whenever the choice must be stable. `RANK` and `DENSE_RANK`
  need none: tied rows get the same value by design.
- **A window function cannot appear in `WHERE`** — it is computed after `WHERE` runs — and
  MySQL has no `QUALIFY`. Compute the rank in a CTE or derived table, then filter it outside.

LC: [178](https://leetcode.com/problems/rank-scores/)

### 1-2) The N-th highest value, `NULL` when there is none

**Deduplicate the values, skip N − 1, take one — and wrap the query in a scalar subquery so
"no such value" comes back as `NULL` instead of an empty result.**

```sql
-- LC 176: second-highest distinct salary
SELECT (SELECT DISTINCT salary
        FROM   Employee
        ORDER  BY salary DESC
        LIMIT  1 OFFSET 1) AS SecondHighestSalary;

-- the same without LIMIT: MAX over an empty set is NULL
SELECT MAX(salary) AS SecondHighestSalary
FROM   Employee
WHERE  salary < (SELECT MAX(salary) FROM Employee);
```

```sql
-- LC 177: N-th highest as a function. LIMIT takes a literal or a variable, never N - 1
CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
DETERMINISTIC
BEGIN
  SET N = N - 1;
  RETURN (
    SELECT DISTINCT salary
    FROM   Employee
    ORDER  BY salary DESC
    LIMIT  1 OFFSET N
  );
END
```

- **`DISTINCT` is the answer to "second highest" on 9000, 8000, 8000** — without it,
  `OFFSET 1` returns the first 8000, which happens to be right, but `OFFSET 2` (third highest)
  returns 8000 again instead of 7000.
- **An empty result is not `NULL`.** A bare `SELECT … LIMIT 1 OFFSET 1` on a one-row table
  returns zero rows, and the problem expects one row holding `null`. The outer `SELECT ( … )`
  or an aggregate (`MAX`) is what turns "nothing" into `NULL`.
- **`LIMIT N - 1` is a syntax error** inside a stored function: `LIMIT` accepts only a literal,
  a parameter or a local variable. Hence `SET N = N - 1` first.

LC: [176](https://leetcode.com/problems/second-highest-salary/),
[177](https://leetcode.com/problems/nth-highest-salary/)

### 1-3) Top-N per group

**Rank inside each group with `PARTITION BY`, then keep the ranks you want. Which ranking
function to use depends on what the question says about ties.**

```sql
-- LC 185: employees earning one of the top three distinct salaries in their department
WITH ranked AS (
  SELECT d.name AS Department,
         e.name AS Employee,
         e.salary AS Salary,
         DENSE_RANK() OVER (PARTITION BY e.departmentId
                            ORDER BY e.salary DESC) AS salary_rank
  FROM   Employee e
  JOIN   Department d ON d.id = e.departmentId
)
SELECT Department, Employee, Salary
FROM   ranked
WHERE  salary_rank <= 3;
```

```sql
-- at most three people per department: ROW_NUMBER with a unique tie-breaker
WITH ranked AS (
  SELECT *,
         ROW_NUMBER() OVER (PARTITION BY departmentId
                            ORDER BY salary DESC, id) AS rn
  FROM   Employee
)
SELECT departmentId, name, salary
FROM   ranked
WHERE  rn <= 3;
```

| "Top 3" means | Filter | On 9000, 8000, 8000, 7000 it keeps |
|---|---|---|
| at most 3 rows | `ROW_NUMBER() <= 3` + tie-breaker | 3 rows |
| the first 3 places, ties included | `RANK() <= 3` | 3 rows (7000 is in 4th place) |
| the top 3 distinct values (LC 185) | `DENSE_RANK() <= 3` | all 4 rows |

Every filter is an upper bound: a department with two people returns two rows under all three.
If only full groups should appear, also compute `COUNT(*) OVER (PARTITION BY departmentId) AS
group_size` in the CTE and keep `group_size >= 3`.

LC 184 (*Department Highest Salary*) is the same query with `salary_rank = 1`, or without a
window:

```sql
-- LC 184: every employee tied for the top salary in their department
SELECT d.name AS Department, e.name AS Employee, e.salary AS Salary
FROM   Employee e
JOIN   Department d ON d.id = e.departmentId
WHERE  (e.departmentId, e.salary) IN (SELECT departmentId, MAX(salary)
                                      FROM   Employee
                                      GROUP  BY departmentId);
```

LC: [184](https://leetcode.com/problems/department-highest-salary/),
[185](https://leetcode.com/problems/department-top-three-salaries/)

### 1-4) First or latest row per group

**Number each group's rows newest (or oldest) first and keep number 1. Use `ROW_NUMBER` when
the answer is one row per group, and `RANK` when every row tied for first must come back.**

```sql
-- the latest activity row per player; (player_id, event_date) is the key, so no tie is possible
WITH ranked AS (
  SELECT *,
         ROW_NUMBER() OVER (PARTITION BY player_id
                            ORDER BY event_date DESC) AS rn
  FROM   Activity
)
SELECT player_id, device_id, event_date
FROM   ranked
WHERE  rn = 1;
```

When the sort column is not unique — two orders on the same `order_date` — add a unique column:
`ORDER BY order_date DESC, order_id DESC`. Without it the chosen row can change between runs.
That makes the pick **deterministic**, not necessarily the latest: the larger `order_id` is the
later order only if ids are assigned in creation order. When it must be the latest, sort by a
finer timestamp (`created_at`) first.

LC 1070 is the case where `ROW_NUMBER` is **wrong**: it asks for every sale in each product's
first year, and a product can have several.

```sql
-- LC 1070: all sales from each product's first year
WITH ranked AS (
  SELECT product_id, year, quantity, price,
         RANK() OVER (PARTITION BY product_id ORDER BY year) AS rnk
  FROM   Sales
)
SELECT product_id, year AS first_year, quantity, price
FROM   ranked
WHERE  rnk = 1;
```

```text
product_id   first_year   quantity   price
100          2008         10         5000
100          2008          3         4000     <- ROW_NUMBER = 1 would drop one of these
200          2011         15         9000
```

- The non-window form is `WHERE (product_id, year) IN (SELECT product_id, MIN(year) … GROUP BY
  product_id)` — it keeps the ties too.
- **`MAX(date)` joined back on the date** is fine when ties should all come back, and wrong
  when the question wants exactly one row: two rows sharing the latest date both match.
- **When only the date is needed, skip the window.** LC 511 asks for each player's first login:
  `SELECT player_id, MIN(event_date) AS first_login FROM Activity GROUP BY player_id`. Number
  the rows only when other columns of that row are needed too, such as its `device_id`.

LC: [511](https://leetcode.com/problems/game-play-analysis-i/),
[1070](https://leetcode.com/problems/product-sales-analysis-iii/)

---

## 2) Duplicates ⭐⭐⭐⭐

### 2-1) Find the duplicated values

**Group by the value and keep the groups with more than one row.**

```sql
-- LC 182
SELECT email AS Email
FROM   Person
GROUP  BY email
HAVING COUNT(*) > 1;
```

- `WHERE` filters rows before grouping; a condition on `COUNT(*)` belongs in `HAVING`.

LC: [182](https://leetcode.com/problems/duplicate-emails/)

### 2-2) Delete duplicates, keep one

**Join the table to itself on the duplicated value and delete the row whose id is larger, so
the smallest id of each value survives.**

```sql
-- LC 196: keep the smallest id per email
DELETE p1
FROM   Person p1
JOIN   Person p2
  ON   p1.email = p2.email
 AND   p1.id > p2.id;
```

The window-function version reads the same numbering the other way round: every row numbered
above 1 is an extra copy.

```sql
-- list the extra copies first ...
WITH numbered AS (
  SELECT id, email,
         ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
  FROM   Person
  WHERE  email IS NOT NULL
)
SELECT id, email
FROM   numbered
WHERE  rn > 1;

-- ... then delete them; the extra derived table is what MySQL needs (see below)
DELETE FROM Person
WHERE  id IN (SELECT id
              FROM  (SELECT id,
                            ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
                     FROM   Person
                     WHERE  email IS NOT NULL) AS numbered
              WHERE  rn > 1);
```

- **Filter out `NULL` emails before numbering.** `PARTITION BY email` puts every `NULL` in one
  partition, so without `email IS NOT NULL` two people with no email would count as duplicates
  and one would be deleted. The self-join above skips them already, because `NULL = NULL` is
  not true; a `GROUP BY email` version, like the window, needs the filter.
- **MySQL error 1093** — *"You can't specify target table 'Person' for update in FROM
  clause"* — is what `DELETE FROM Person WHERE id NOT IN (SELECT MIN(id) FROM Person GROUP BY
  email)` raises. Wrapping the subquery in one more derived table
  (`SELECT id FROM (SELECT MIN(id) AS id … ) AS keep`) makes MySQL materialise it first, and
  the statement runs.
- `SELECT DISTINCT` removes rows identical in **every** column; it is not a dedupe by key and
  it changes nothing in the table.

LC: [196](https://leetcode.com/problems/delete-duplicate-emails/)

### 2-3) Values that appear exactly once

**`HAVING COUNT(*) = 1` finds them; wrapping the result in `MAX` returns `NULL` when there are
none.**

```sql
-- LC 619: the largest number that appears only once, NULL if every number repeats
SELECT MAX(num) AS num
FROM  (SELECT num
       FROM   MyNumbers
       GROUP  BY num
       HAVING COUNT(*) = 1) AS singles;
```

LC: [619](https://leetcode.com/problems/biggest-single-number/)

---

## 3) NULLs and Missing Rows ⭐⭐⭐⭐⭐

### 3-1) `<>` and `=` drop `NULL` rows

**Any comparison with `NULL` is `UNKNOWN`, and `WHERE` keeps only `TRUE` — so "not referred by
customer 2" must name the `NULL` rows explicitly.**

```sql
-- LC 584: customers not referred by id 2, including those referred by nobody
SELECT name
FROM   Customer
WHERE  referee_id <> 2
   OR  referee_id IS NULL;

-- the same with MySQL's NULL-safe equality operator
SELECT name
FROM   Customer
WHERE  NOT (referee_id <=> 2);
```

- `referee_id <> 2` alone returns only Zack: the three customers with no referee are
  `UNKNOWN`, not `TRUE`, and disappear.
- `<=>` treats `NULL <=> NULL` as true and `NULL <=> 2` as false, so it never returns
  `UNKNOWN`. Standard SQL spells it `IS NOT DISTINCT FROM`.

LC: [584](https://leetcode.com/problems/find-customer-referee/)

### 3-2) "Never did X": the anti-join

**`LEFT JOIN` the activity table and keep the rows where it matched nothing — or use
`NOT EXISTS`. Avoid `NOT IN` against a column that can be `NULL`.**

```sql
-- LC 183: customers who never placed an order
SELECT c.name AS Customers
FROM   Customers c
LEFT   JOIN Orders o ON o.customerId = c.id
WHERE  o.id IS NULL;

-- the same with NOT EXISTS
SELECT c.name AS Customers
FROM   Customers c
WHERE  NOT EXISTS (SELECT 1 FROM Orders o WHERE o.customerId = c.id);
```

```sql
-- why NOT IN is dangerous: one NULL in the list makes every comparison UNKNOWN
SELECT 3 NOT IN (1, 2)    AS no_null_in_list,
       3 NOT IN (1, NULL) AS null_in_list;
```

```text
no_null_in_list   null_in_list
1                 NULL
```

- So `WHERE id NOT IN (SELECT customerId FROM Orders)` returns **no rows at all** the moment
  one order has a `NULL` `customerId`. `NOT EXISTS` and the `LEFT JOIN … IS NULL` form are
  unaffected.
- In the `LEFT JOIN` form, test a column that is never `NULL` in a real match — the joined
  table's primary key — not a nullable attribute.

LC: [183](https://leetcode.com/problems/customers-who-never-order/)

### 3-3) Keep the rows with zero matches

**Build every row the answer must contain first — often with a `CROSS JOIN` — then `LEFT
JOIN` the facts and count a column from the right side, so a missing match counts as 0.**

```sql
-- LC 1280: how many times each student attended each subject's exam, zeros included
SELECT s.student_id, s.student_name, sub.subject_name,
       COUNT(e.student_id) AS attended_exams
FROM   Students s
CROSS  JOIN Subjects sub
LEFT   JOIN Examinations e
  ON   e.student_id = s.student_id
 AND   e.subject_name = sub.subject_name
GROUP  BY s.student_id, s.student_name, sub.subject_name
ORDER  BY s.student_id, sub.subject_name;
```

```text
student_id   student_name   subject_name   attended_exams
1            Alice          Math           2
1            Alice          Physics        1
2            Bob            Math           1
2            Bob            Physics        0
```

- **`COUNT(*)` would report Bob's Physics as 1**: the `LEFT JOIN` still produces one row for
  him, with `NULL`s on the right. `COUNT(column)` skips `NULL`s, so it reports 0.
- **A condition on the right table goes in `ON`, not `WHERE`.** `WHERE e.subject_name = …`
  throws away the `NULL` rows the `LEFT JOIN` just kept, turning it back into an inner join.

When the answer is a ratio rather than a count, the empty group gives `NULL`, and
`IFNULL(…, 0)` turns it into the 0 the problem asks for:

```sql
-- LC 1251: average selling price per product; a product with no sales reports 0
SELECT p.product_id,
       IFNULL(ROUND(SUM(p.price * u.units) / SUM(u.units), 2), 0) AS average_price
FROM   Prices p
LEFT   JOIN UnitsSold u
  ON   u.product_id = p.product_id
 AND   u.purchase_date BETWEEN p.start_date AND p.end_date
GROUP  BY p.product_id;
```

- `BETWEEN` is inclusive at both ends, which is what a price period's `start_date` and
  `end_date` mean here.

LC: [1251](https://leetcode.com/problems/average-selling-price/),
[1280](https://leetcode.com/problems/students-and-examinations/)

### 3-4) `COUNT(*)` vs `COUNT(column)` after a `LEFT JOIN`

**`COUNT(*)` counts every row that reaches it, `NULL`s included; `COUNT(column)` counts only
the rows where that column is not `NULL`. Both count only the rows that survive `WHERE`,
because `WHERE` runs before any aggregate — so a `WHERE` on the right table of a `LEFT JOIN`
deletes the unmatched rows that `COUNT(*)` was supposed to count.**

```text
FROM / JOIN … ON    build the joined rows; a LEFT JOIN keeps unmatched left rows, NULL on the right
WHERE               drop rows                       <- before any COUNT
GROUP BY            form the groups
HAVING              drop groups                     <- after COUNT, so it can filter on one
SELECT              COUNT(*), COUNT(col), …
ORDER BY / LIMIT
```

| Expression | Counts | An unmatched row of a `LEFT JOIN` |
|---|---|---|
| `COUNT(*)` (same as `COUNT(1)`) | every row | counts 1 |
| `COUNT(a.col)` | rows where `a.col` is not `NULL` | counts 0 |
| `COUNT(DISTINCT a.col)` | distinct non-`NULL` values of `a.col` | counts 0 |

LC 550 divides one by the other. On the problem's own example the `LEFT JOIN` hands `COUNT`
three rows, one per player, and only player 1 found a next-day row:

```sql
-- LC 550: the rows the LEFT JOIN produces, before any COUNT
WITH first_login AS (
  SELECT player_id, MIN(event_date) AS first_day
  FROM   Activity
  GROUP  BY player_id
)
SELECT f.player_id, f.first_day, a.player_id AS next_day_player, a.event_date
FROM   first_login f
LEFT   JOIN Activity a
  ON   a.player_id = f.player_id
 AND   a.event_date = f.first_day + INTERVAL 1 DAY;
```

```text
player_id   first_day    next_day_player   event_date
1           2016-03-01   1                 2016-03-02
2           2017-06-25   NULL              NULL
3           2016-03-02   NULL              NULL
```

`COUNT(a.player_id)` is 1 (the returning players), `COUNT(*)` is 3 (all players), and
`ROUND(1 / 3, 2)` is the expected `0.33`.

Move the next-day test from `ON` into `WHERE` and the denominator collapses:

```sql
-- WRONG: the right-table condition in WHERE runs before COUNT and removes players 2 and 3
WITH first_login AS (
  SELECT player_id, MIN(event_date) AS first_day
  FROM   Activity
  GROUP  BY player_id
)
SELECT ROUND(COUNT(a.player_id) / COUNT(*), 2) AS fraction   -- 1.00, not 0.33
FROM   first_login f
LEFT   JOIN Activity a ON a.player_id = f.player_id
WHERE  a.event_date = f.first_day + INTERVAL 1 DAY;
```

- **`COUNT(*)` ignores `NULL`s but not `WHERE`.** It never skips a row for holding `NULL`, but
  it only sees the rows `WHERE` let through. `WHERE a.player_id IS NOT NULL` after the correct
  join gives the same wrong `1.00`: it is an inner join with extra steps.
- **A filter on the aggregate goes in `HAVING`**, which runs after `COUNT`; `WHERE COUNT(*) > 1`
  is a syntax error.
- **`DISTINCT` in the numerator is needed only when one left row can match several right
  rows.** In LC 550 `(player_id, event_date)` is the primary key, so a player has at most one
  next-day row and `COUNT(a.player_id)` is already a player count. In a raw login log with
  several rows per day, both counts inflate: write
  `COUNT(DISTINCT a.player_id) / COUNT(DISTINCT f.player_id)`.
- The same rule decides LC 1280's zeros — see [3-3](#3-3-keep-the-rows-with-zero-matches).

LC: [550](https://leetcode.com/problems/game-play-analysis-iv/),
[1280](https://leetcode.com/problems/students-and-examinations/)

---

## 4) Aggregation ⭐⭐⭐⭐⭐

### 4-1) Conditional aggregation

**Put the condition inside the aggregate — `SUM(CASE WHEN … THEN … END)` — to compute several
filtered totals in one pass over the table.**

```sql
-- LC 1193: per month and country, all transactions and the approved ones
SELECT DATE_FORMAT(trans_date, '%Y-%m')         AS month,
       country,
       COUNT(*)                                 AS trans_count,
       SUM(state = 'approved')                  AS approved_count,
       SUM(amount)                              AS trans_total_amount,
       SUM(IF(state = 'approved', amount, 0))   AS approved_total_amount
FROM   Transactions
GROUP  BY month, country;
```

- In MySQL a comparison is the number 1 or 0, so `SUM(state = 'approved')` counts the matching
  rows. The portable spelling is `SUM(CASE WHEN state = 'approved' THEN 1 ELSE 0 END)`.
- **`ELSE 0` matters for a sum that must be 0**, and must be **left out** when the problem
  wants `null` for an empty bucket — see the pivot in [4-4](#4-4-pivot-and-unpivot).
- MySQL lets `GROUP BY` name a `SELECT` alias (`month`); most other databases do not.

LC: [1193](https://leetcode.com/problems/monthly-transactions-i/)

### 4-2) Rates and percentages

**A rate is the average of a 0/1 condition: `AVG(condition)` is the share of rows where it
holds. Round once, at the end.**

```sql
-- LC 1934: each user's share of confirmed messages; users with no messages report 0
SELECT s.user_id,
       ROUND(IFNULL(AVG(c.action = 'confirmed'), 0), 2) AS confirmation_rate
FROM   Signups s
LEFT   JOIN Confirmations c ON c.user_id = s.user_id
GROUP  BY s.user_id;
```

```sql
-- LC 1211: two averages per query name, one of them a percentage
SELECT query_name,
       ROUND(AVG(rating / position), 2)  AS quality,
       ROUND(AVG(rating < 3) * 100, 2)   AS poor_query_percentage
FROM   Queries
WHERE  query_name IS NOT NULL      -- LeetCode's tests include rows with no query name
GROUP  BY query_name;
```

- **MySQL's `/` is decimal division** (`7 / 2` is `3.5000`); `DIV` is the integer one. The
  integer-division bug that breaks the same query in SQL Server or PostgreSQL does not happen
  here.
- **Round the final value, never the parts.** `ROUND(a, 2) / ROUND(b, 2)` drifts from the
  expected answer in the last digit.
- **`AVG` skips `NULL`.** `AVG(condition)` is the share of rows only when the condition is
  never `NULL`: one `TRUE` row and one `NULL` row average to 1, not 0.5. When every row belongs
  in the denominator, map `NULL` to 0 first — `AVG(IFNULL(condition, 0))`. In LC 1934 the
  `NULL` comes only from a user with no messages at all, whose whole average is `NULL`; the
  outer `IFNULL(…, 0)` handles that.
- **Check the denominator's grain.** "Fraction of players" divides by
  `COUNT(DISTINCT player_id)`, not by the number of rows (see [5-4](#5-4-retention-the-day-after-the-first-login)).

LC: [1211](https://leetcode.com/problems/queries-quality-and-percentage/),
[1934](https://leetcode.com/problems/confirmation-rate/)

### 4-3) "Has all of them": relational division

**Count the distinct things each entity has and compare with the total number that exist.**

```sql
-- LC 1045: customers who bought every product
SELECT customer_id
FROM   Customer
GROUP  BY customer_id
HAVING COUNT(DISTINCT product_key) = (SELECT COUNT(*) FROM Product);
```

- `DISTINCT` is essential: a customer who bought product 5 twice and never bought product 6
  has two rows, and `COUNT(*) = 2` would pass them.

LC: [1045](https://leetcode.com/problems/customers-who-bought-all-products/)

### 4-4) Pivot and unpivot

**Pivot rows into columns with one conditional aggregate per output column; unpivot columns
into rows with one `SELECT` per column joined by `UNION ALL`.**

```sql
-- LC 1179: one row per department, one revenue column per month (Jan to Dec)
SELECT id,
       SUM(CASE WHEN month = 'Jan' THEN revenue END) AS Jan_Revenue,
       SUM(CASE WHEN month = 'Feb' THEN revenue END) AS Feb_Revenue,
       SUM(CASE WHEN month = 'Mar' THEN revenue END) AS Mar_Revenue,
       SUM(CASE WHEN month = 'Apr' THEN revenue END) AS Apr_Revenue,
       SUM(CASE WHEN month = 'May' THEN revenue END) AS May_Revenue,
       SUM(CASE WHEN month = 'Jun' THEN revenue END) AS Jun_Revenue,
       SUM(CASE WHEN month = 'Jul' THEN revenue END) AS Jul_Revenue,
       SUM(CASE WHEN month = 'Aug' THEN revenue END) AS Aug_Revenue,
       SUM(CASE WHEN month = 'Sep' THEN revenue END) AS Sep_Revenue,
       SUM(CASE WHEN month = 'Oct' THEN revenue END) AS Oct_Revenue,
       SUM(CASE WHEN month = 'Nov' THEN revenue END) AS Nov_Revenue,
       SUM(CASE WHEN month = 'Dec' THEN revenue END) AS Dec_Revenue
FROM   Department
GROUP  BY id;
```

```text
id   Jan_Revenue   Feb_Revenue   Mar_Revenue   Apr_Revenue … Dec_Revenue
1    8000          7000          6000          NULL … NULL
2    9000          NULL          NULL          NULL … NULL
3    NULL          10000         NULL          NULL … NULL
```

```sql
-- LC 1795: one row per (product, store) that sells it
SELECT product_id, 'store1' AS store, store1 AS price FROM Products WHERE store1 IS NOT NULL
UNION ALL
SELECT product_id, 'store2', store2 FROM Products WHERE store2 IS NOT NULL
UNION ALL
SELECT product_id, 'store3', store3 FROM Products WHERE store3 IS NOT NULL;
```

- The pivot has **no `ELSE 0`**: LC 1179 expects `null` for a month with no revenue, and a
  `CASE` without `ELSE` yields `NULL`, which `SUM` ignores.
- The unpivot uses `UNION ALL`. Plain `UNION` would give the same rows here, since
  `(product_id, store)` already makes each one unique, but it adds a deduplicating sort for
  nothing — and on rows that are not unique it silently drops real ones.

LC: [1179](https://leetcode.com/problems/reformat-department-table/),
[1795](https://leetcode.com/problems/rearrange-products-table/)

### 4-5) Strings: `GROUP_CONCAT`, `CONCAT`, `LIKE`

**`GROUP_CONCAT` turns a group's values into one string; `CONCAT`, `UPPER`, `LOWER` and
`SUBSTRING` rebuild a value; `LIKE` with a space in the pattern matches a whole word.**

```sql
-- LC 1484: per day, how many distinct products sold, and their names in order
SELECT sell_date,
       COUNT(DISTINCT product) AS num_sold,
       GROUP_CONCAT(DISTINCT product ORDER BY product SEPARATOR ',') AS products
FROM   Activities
GROUP  BY sell_date
ORDER  BY sell_date;
```

```sql
-- LC 1667: capitalise the first letter only
SELECT user_id,
       CONCAT(UPPER(LEFT(name, 1)), LOWER(SUBSTRING(name, 2))) AS name
FROM   Users
ORDER  BY user_id;
```

```sql
-- LC 1527: a condition code starting with DIAB1, as its own word
SELECT patient_id, patient_name, conditions
FROM   Patients
WHERE  conditions LIKE 'DIAB1%'
   OR  conditions LIKE '% DIAB1%';
```

- `'%DIAB1%'` also matches `SADIAB100`, where `DIAB1` sits inside another word. The two
  patterns match it only at the start of the string or after a space.
- `GROUP_CONCAT` output is capped at `group_concat_max_len` (1024 bytes by default) and is cut
  off silently beyond it — irrelevant on LeetCode, worth knowing in production.

LC: [1484](https://leetcode.com/problems/group-sold-products-by-the-date/),
[1527](https://leetcode.com/problems/patients-with-a-condition/),
[1667](https://leetcode.com/problems/fix-names-in-a-table/)

---

## 5) Comparing Rows ⭐⭐⭐⭐⭐

### 5-1) "Higher than the day before": join on the date, not on the row before

**"Yesterday" is a date, not the previous row. Join the table to itself on
`date - INTERVAL 1 DAY`, so a missing day produces no match.**

```sql
-- LC 197: days warmer than the calendar day before
SELECT w.id
FROM   Weather w
JOIN   Weather y ON y.recordDate = w.recordDate - INTERVAL 1 DAY
WHERE  w.temperature > y.temperature;
```

```text
id   recordDate   temperature
1    2015-01-01   10
2    2015-01-02   25      <- warmer than Jan 1: returned
3    2015-01-03   20
4    2015-01-05   30      <- no Jan 4 row: not returned
```

- **`LAG(temperature) OVER (ORDER BY recordDate)` gets this wrong**: it compares Jan 5 with
  Jan 3, the previous *row*, and returns id 4. Use `LAG` only when the question means the
  previous row, or check the gap too (`DATEDIFF(recordDate, LAG(recordDate) OVER (…)) = 1`).
- `DATEDIFF(a, b)` is `a − b` in days — the argument order matters.
- Never join on `id - 1`; ids are not dates and need not be consecutive.

LC: [197](https://leetcode.com/problems/rising-temperature/)

### 5-2) The same value N times in a row: `LAG` / `LEAD`

**Look back with `LAG(x, 1)` and `LAG(x, 2)` and keep the rows where all three agree.**

```sql
-- LC 180: numbers that appear at least three times consecutively
SELECT DISTINCT num AS ConsecutiveNums
FROM  (SELECT num,
              LAG(num, 1) OVER (ORDER BY id) AS prev1,
              LAG(num, 2) OVER (ORDER BY id) AS prev2
       FROM   Logs) AS t
WHERE  num = prev1
  AND  num = prev2;
```

- `DISTINCT` is needed: a run of four `1`s matches twice.
- The self-join version `l2.id = l1.id + 1 AND l3.id = l1.id + 2` assumes ids have no gaps;
  `LAG` ordered by `id` does not.
- For "N in a row" with a large or variable N, use gaps and islands instead ([5-3](#5-3-gaps-and-islands)).

LC: [180](https://leetcode.com/problems/consecutive-numbers/)

### 5-3) Gaps and islands

**Number the qualifying rows in order and subtract that number from the id (or date): rows in
one unbroken run share the same difference, so the difference is the run's group key.**

```sql
-- LC 601: rows in a run of 3+ consecutive ids where people >= 100
WITH busy AS (
  SELECT *,
         CAST(id AS SIGNED) - CAST(ROW_NUMBER() OVER (ORDER BY id) AS SIGNED) AS grp
  FROM   Stadium
  WHERE  people >= 100
), runs AS (
  SELECT *,
         COUNT(*) OVER (PARTITION BY grp) AS run_length
  FROM   busy
)
SELECT id, visit_date, people
FROM   runs
WHERE  run_length >= 3
ORDER  BY visit_date;
```

```text
id   people   rn   id - rn
2    109      1    1
3    150      2    1        <- run of 2: dropped
5    145      3    2
6    1455     4    2
7    199      5    2
8    188      6    2        <- run of 4: kept
```

- For dates, the key is `DATE_SUB(visit_date, INTERVAL rn DAY)` — consecutive days minus
  consecutive numbers give the same date.
- **Why the `CAST`s: in MySQL `ROW_NUMBER()` is `BIGINT UNSIGNED`**, so a bare
  `id - ROW_NUMBER()` raises *"BIGINT UNSIGNED value is out of range"* the moment the result
  would be negative — for ids that start at 0, the very first row computes `0 - 1`. Casting
  both sides to `SIGNED` keeps the grouping and allows any id.
- The run's start and end are `MIN(id)` and `MAX(id)` grouped by `grp` — LC 1285 asks for
  exactly that.

LC: [601](https://leetcode.com/problems/human-traffic-of-stadium/),
[1285](https://leetcode.com/problems/find-the-start-and-end-number-of-continuous-ranges/)

### 5-4) Retention: the day after the first login

**Find each player's first day, then look for a row on the day after it, and divide by the
number of players.**

```sql
-- LC 550: fraction of players who logged in again the day after their first login
WITH first_login AS (
  SELECT player_id, MIN(event_date) AS first_day
  FROM   Activity
  GROUP  BY player_id
)
SELECT ROUND(COUNT(a.player_id) / COUNT(*), 2) AS fraction
FROM   first_login f
LEFT   JOIN Activity a
  ON   a.player_id = f.player_id
 AND   a.event_date = f.first_day + INTERVAL 1 DAY;
```

- `first_login` has one row per player, so `COUNT(*)` is the number of players, and
  `COUNT(a.player_id)` counts only the players whose next-day row was found.
- Keep the next-day test in `ON`. In `WHERE` it removes the players who never came back from
  the denominator and the answer becomes `1.00` — see [3-4](#3-4-count-vs-countcolumn-after-a-left-join).
- `DATEDIFF(a.event_date, f.first_day) = 1` is the same test; the answer is a fraction
  (`0.33`), not a percentage.
- "Logged in the day after **any** login" is a different, easier question; this one anchors
  on the first day only.

LC: [550](https://leetcode.com/problems/game-play-analysis-iv/)

---

## 6) Running Totals and Moving Windows ⭐⭐⭐⭐

### 6-1) Running total

**`SUM(x) OVER (ORDER BY …)` is the total of every row up to and including this one.**

```sql
-- LC 1204: the last person who fits on a bus with a 1000 kg limit, boarding by turn
WITH boarding AS (
  SELECT person_name,
         turn,
         SUM(weight) OVER (ORDER BY turn) AS total_weight
  FROM   Queue
)
SELECT person_name
FROM   boarding
WHERE  total_weight <= 1000
ORDER  BY turn DESC
LIMIT  1;
```

- With `ORDER BY` and no frame, the default frame is `RANGE BETWEEN UNBOUNDED PRECEDING AND
  CURRENT ROW`, which includes every row **tied** with the current one. Here `turn` is unique,
  so it does not matter; on a non-unique sort key, write `ROWS BETWEEN UNBOUNDED PRECEDING AND
  CURRENT ROW` or add a tie-breaker.

LC: [1204](https://leetcode.com/problems/last-person-to-fit-in-the-bus/)

### 6-2) Moving sum and average over N days

**Collapse to one row per day first, then use a frame. A `RANGE` frame over the date counts
calendar days, so a missing day cannot stretch the window.**

```sql
-- LC 1321: 7-day moving total and average, from the 7th day on
WITH daily AS (
  SELECT visited_on, SUM(amount) AS amount
  FROM   Customer
  GROUP  BY visited_on
), windowed AS (
  SELECT visited_on,
         SUM(amount) OVER (ORDER BY visited_on
                           RANGE BETWEEN INTERVAL 6 DAY PRECEDING AND CURRENT ROW) AS amount,
         MIN(visited_on) OVER () AS first_day
  FROM   daily
)
SELECT visited_on,
       amount,
       ROUND(amount / 7, 2) AS average_amount
FROM   windowed
WHERE  visited_on >= first_day + INTERVAL 6 DAY
ORDER  BY visited_on;
```

```text
visited_on   amount   average_amount
2019-01-07   860      122.86
2019-01-08   840      120.00
2019-01-09   840      120.00
2019-01-10   1000     142.86
```

- **Group by day first.** The table has two visits on Jan 10. Over raw rows, a `ROWS` frame
  would count them as two of the seven "days"; the `RANGE` frame would still sum the right
  seven calendar days, but once per visit, so Jan 10 would appear twice in the output. Grouping
  first gives one row per day.
- `ROWS BETWEEN 6 PRECEDING AND CURRENT ROW` means the last seven **rows**, which equals seven
  days only when no day is missing. `RANGE … INTERVAL 6 DAY` is correct either way.
- The `WHERE` on `first_day` drops the first six days, whose windows are not full yet.

LC: [1321](https://leetcode.com/problems/restaurant-growth/)

---

## 7) Updating and Reshaping ⭐⭐⭐

### 7-1) `UPDATE` with `CASE`

**One `UPDATE` with a `CASE` swaps values in a single statement, with no temporary value.**

```sql
-- LC 627: swap every 'm' and 'f'
UPDATE Salary
SET    sex = CASE sex WHEN 'm' THEN 'f' ELSE 'm' END;
```

- Two statements (`'m'` → `'f'`, then `'f'` → `'m'`) would turn everyone into `'m'`; a single
  `UPDATE` evaluates `CASE` against each row's original value.

LC: [627](https://leetcode.com/problems/swap-salary/)

### 7-2) Swap adjacent rows

**For odd ids take the next row's value, for even ids the previous row's; the last odd id has
no partner and keeps its own.**

```sql
-- LC 626: swap every two consecutive students
SELECT id,
       CASE
         WHEN id % 2 = 1 THEN COALESCE(LEAD(student) OVER (ORDER BY id), student)
         ELSE LAG(student) OVER (ORDER BY id)
       END AS student
FROM   Seat
ORDER  BY id;
```

```sql
-- the same by rewriting the ids instead of the names
SELECT CASE
         WHEN id % 2 = 1 AND id = (SELECT COUNT(*) FROM Seat) THEN id
         WHEN id % 2 = 1 THEN id + 1
         ELSE id - 1
       END AS id,
       student
FROM   Seat
ORDER  BY id;
```

- Both forms rely on the ids being exactly 1..n, which LC 626 guarantees: the second rewrites
  them, and the first picks each row's partner from `id % 2`. With ids 1, 3, 4 the first form
  pairs the wrong rows. For arbitrary ids, compute `ROW_NUMBER() OVER (ORDER BY id)` in a CTE and
  use it in place of `id` in the parity test.

LC: [626](https://leetcode.com/problems/exchange-seats/)

### 7-3) Count both sides of a pair: `UNION ALL`

**Stack both columns into one with `UNION ALL`, then group — each accepted request is a friend
for both people.**

```sql
-- LC 602: the person with the most friends
WITH ids AS (
  SELECT requester_id AS id FROM RequestAccepted
  UNION ALL
  SELECT accepter_id FROM RequestAccepted
)
SELECT id, COUNT(*) AS num
FROM   ids
GROUP  BY id
ORDER  BY num DESC
LIMIT  1;
```

- **`UNION` here is a bug**: it removes duplicates, so person 3 — in three requests — would
  appear once and count 1. `UNION ALL` keeps every row and is also faster, because it skips
  the dedupe.

LC: [602](https://leetcode.com/problems/friend-requests-ii-who-has-the-most-friends/)

---

## 8) MySQL Details That Fail a Submission ⭐⭐⭐⭐

```sql
SELECT 7 / 2                                   AS decimal_div,
       7 DIV 2                                 AS integer_div,
       ROUND(2 / 3, 2)                         AS rounded,
       DATEDIFF('2024-03-01', '2024-02-28')    AS days_between,
       DATE('2024-02-28') + INTERVAL 1 DAY     AS next_day,
       DATE_FORMAT('2024-02-28', '%Y-%m')      AS month_bucket;
```

```text
decimal_div   integer_div   rounded   days_between   next_day     month_bucket
3.5000        3             0.67      2              2024-02-29   2024-02
```

| Detail | What happens | Do this |
|---|---|---|
| `/` vs `DIV` | `/` is decimal, `DIV` truncates | `/` for rates, then `ROUND(x, 2)` |
| `DATEDIFF(a, b)` | `a − b` in whole days | put the later date first |
| Date arithmetic | `d + INTERVAL 1 DAY` handles month ends and leap years | never add 1 to a date as a number |
| Month bucket | `DATE_FORMAT(d, '%Y-%m')` → `2024-02` | group by the formatted value |
| Window function in `WHERE` | not allowed, and MySQL has no `QUALIFY` | CTE or derived table, filter outside |
| Reserved words as aliases | `rank`, `row_number` and `year_month` (an `INTERVAL` unit) are reserved in MySQL 8 | backticks, `` AS `rank` ``, or a different name |
| `LIMIT` | takes literals or variables, not `N - 1` | `SET N = N - 1;` first ([1-2](#1-2-the-n-th-highest-value-null-when-there-is-none)) |
| Error 1093 | cannot read the table being updated or deleted in a subquery | wrap it in a derived table ([2-2](#2-2-delete-duplicates-keep-one)) |
| `ONLY_FULL_GROUP_BY` | on in MySQL 8's default `sql_mode`: a selected column must be grouped or aggregated | group by every non-aggregated column |
| `GROUP BY` alias | MySQL accepts `GROUP BY month` for a `SELECT` alias | fine here; other databases may reject it |
| `UNION` vs `UNION ALL` | `UNION` deduplicates | `UNION ALL` unless duplicates must go ([7-3](#7-3-count-both-sides-of-a-pair-union-all)) |
| `ROW_NUMBER()` arithmetic | returns `BIGINT UNSIGNED`; a negative difference errors | `CAST(… AS SIGNED)` ([5-3](#5-3-gaps-and-islands)) |
| `NULL` comparisons | `= NULL`, `<> 2` and `NOT IN (…, NULL)` drop rows | `IS NULL`, `<=>`, `NOT EXISTS` ([3](#3-nulls-and-missing-rows-)) |

---

## 9) Trick → Problem Index ⭐⭐⭐

| Trick | Problems | Section |
|---|---|---|
| `DENSE_RANK`, N-th distinct value | 176, 177, 178, 185 | [1-1](#1-1-row_number-vs-rank-vs-dense_rank), [1-2](#1-2-the-n-th-highest-value-null-when-there-is-none) |
| Top-N / highest per group | 184, 185 | [1-3](#1-3-top-n-per-group) |
| First or latest row per group | 511, 1070 | [1-4](#1-4-first-or-latest-row-per-group) |
| Find / delete duplicates | 182, 196 | [2-1](#2-1-find-the-duplicated-values), [2-2](#2-2-delete-duplicates-keep-one) |
| `HAVING COUNT(*) = 1`, `MAX` for `NULL` | 619 | [2-3](#2-3-values-that-appear-exactly-once) |
| `NULL` in `WHERE`, `<=>` | 584 | [3-1](#3-1--and--drop-null-rows) |
| Anti-join | 183 | [3-2](#3-2-never-did-x-the-anti-join) |
| `CROSS JOIN` + `LEFT JOIN` + `COUNT(col)`, `IFNULL` | 1251, 1280 | [3-3](#3-3-keep-the-rows-with-zero-matches) |
| `COUNT(*)` vs `COUNT(col)`, `WHERE` runs before `COUNT` | 550, 1280 | [3-4](#3-4-count-vs-countcolumn-after-a-left-join) |
| Conditional aggregation | 1193 | [4-1](#4-1-conditional-aggregation) |
| `AVG(condition)` rates | 1211, 1934 | [4-2](#4-2-rates-and-percentages) |
| Relational division | 1045 | [4-3](#4-3-has-all-of-them-relational-division) |
| Pivot / unpivot | 1179, 1795 | [4-4](#4-4-pivot-and-unpivot) |
| `GROUP_CONCAT`, string functions, `LIKE` | 1484, 1527, 1667 | [4-5](#4-5-strings-group_concat-concat-like) |
| Date self-join | 197 | [5-1](#5-1-higher-than-the-day-before-join-on-the-date-not-on-the-row-before) |
| `LAG` / `LEAD` | 180, 626 | [5-2](#5-2-the-same-value-n-times-in-a-row-lag--lead), [7-2](#7-2-swap-adjacent-rows) |
| Gaps and islands | 601, 1285 | [5-3](#5-3-gaps-and-islands) |
| Retention | 550 | [5-4](#5-4-retention-the-day-after-the-first-login) |
| Running total | 1204 | [6-1](#6-1-running-total) |
| Moving window | 1321 | [6-2](#6-2-moving-sum-and-average-over-n-days) |
| `UPDATE … CASE` | 627 | [7-1](#7-1-update-with-case) |
| `UNION ALL` both columns | 602 | [7-3](#7-3-count-both-sides-of-a-pair-union-all) |
