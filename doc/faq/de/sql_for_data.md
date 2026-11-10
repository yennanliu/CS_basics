# SQL for Data Engineering

> **Scope** — analytical SQL as a data engineering interview tests it: how a query is evaluated, joins and their algorithms, NULL semantics, aggregation, window functions, CTEs and recursion, the classic interview query patterns, MERGE and upserts, dates and time zones, query performance, and the dialect differences between PostgreSQL, BigQuery, Snowflake and Spark SQL. Table design is [schema_design.md](./schema_design.md)'s; choosing a database is [sql_vs_nosql.md](./sql_vs_nosql.md)'s.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`./data_modeling.md`](./data_modeling.md) — the star schemas these queries run on; [`./spark.md`](./spark.md) — how Spark SQL executes them; [`../sql/faq_sql.md`](../sql/faq_sql.md) — the older, longer SQL notes.

Every query is **PostgreSQL** unless the first line of its fence names another dialect. Each
section leads with the sentence you would say out loud, then the mechanism, then the trade-off
the interviewer asks about next.

---

## 0) The Sample Tables

Every query below runs against these four tables. Interviewers usually hand you something
very close to them, so it is worth recognising the shapes.

```sql
-- PostgreSQL
CREATE TABLE users (
  user_id      INT PRIMARY KEY,
  signup_date  DATE NOT NULL,
  country      TEXT
);

CREATE TABLE orders (
  order_id     INT PRIMARY KEY,
  user_id      INT NOT NULL REFERENCES users (user_id),
  order_date   DATE NOT NULL,
  amount       NUMERIC(10, 2) NOT NULL,
  status       TEXT NOT NULL            -- 'paid', 'refunded', 'cancelled'
);

CREATE TABLE events (
  user_id      INT NOT NULL,
  event_ts     TIMESTAMP NOT NULL,      -- UTC
  event_name   TEXT NOT NULL            -- 'view', 'cart', 'purchase'
);

CREATE TABLE employees (
  emp_id       INT PRIMARY KEY,
  name         TEXT NOT NULL,
  dept         TEXT NOT NULL,
  salary       NUMERIC(10, 2) NOT NULL,
  manager_id   INT REFERENCES employees (emp_id)   -- NULL for the CEO
);
```

---

## 1) How a Query Is Evaluated ⭐⭐⭐⭐⭐

### 1-1) Logical execution order

**SQL is written `SELECT` first but evaluated `FROM` first — and almost every "why does this
not work" question is answered by the order below.**

```text
1  FROM / JOIN / ON     build the row set
2  WHERE                filter rows            (no aggregates, no window functions)
3  GROUP BY             form groups
4  HAVING               filter groups          (aggregates allowed)
5  window functions     computed over the remaining rows
6  QUALIFY              filter on window results   (Snowflake, BigQuery, Databricks, DuckDB)
7  SELECT               compute output columns and aliases
8  DISTINCT             remove duplicate output rows
9  ORDER BY             sort                   (aliases allowed)
10 LIMIT / OFFSET       cut
```

Consequences you should be able to state:

- A `SELECT` alias cannot be used in `WHERE` — `WHERE` runs before `SELECT`. (Snowflake is an
  exception: it resolves aliases in `WHERE`.)
- Aliases **can** be used in `ORDER BY`, and PostgreSQL, BigQuery, Snowflake and Spark also
  accept them in `GROUP BY` as an extension.
- An aggregate cannot appear in `WHERE`; filter on it in `HAVING`.
- A window function cannot appear in `WHERE`, `GROUP BY` or `HAVING`, because it is computed
  after them. Wrap the query in a CTE, or use `QUALIFY`.
- This is the **logical** order. The optimiser may physically reorder — push a filter below a
  join, aggregate before a join — as long as the result is the same.

### 1-2) `WHERE` vs `HAVING` vs `QUALIFY`

| Clause | Filters | Can reference | Runs |
|---|---|---|---|
| `WHERE` | input rows | columns of the `FROM` row set | before grouping |
| `HAVING` | groups | aggregates and grouping columns | after grouping |
| `QUALIFY` | output rows | window function results | after windows |

```sql
-- PostgreSQL: users whose paid revenue in 2026 is above 1,000
SELECT user_id, SUM(amount) AS revenue
FROM   orders
WHERE  status = 'paid'                       -- row filter: cheap, before grouping
  AND  order_date >= DATE '2026-01-01'
GROUP  BY user_id
HAVING SUM(amount) > 1000;                   -- group filter
```

Putting a row condition in `HAVING` instead of `WHERE` is legal for grouping columns but
groups every row first — always filter as early as possible.

---

## 2) Joins ⭐⭐⭐⭐⭐

### 2-1) The join types

**A join pairs rows from two tables on a condition; the type decides what happens to rows
that find no partner.**

| Join | Keeps | Unmatched side filled with |
|---|---|---|
| `INNER JOIN` | only matched pairs | — |
| `LEFT [OUTER] JOIN` | every left row | `NULL` on the right |
| `RIGHT [OUTER] JOIN` | every right row | `NULL` on the left |
| `FULL [OUTER] JOIN` | every row from both | `NULL` on the missing side |
| `CROSS JOIN` | every pair (Cartesian product) | — |
| semi-join (`EXISTS`, `IN`) | left rows that have a match, each once | — |
| anti-join (`NOT EXISTS`) | left rows with **no** match | — |

- `RIGHT JOIN` is a `LEFT JOIN` with the tables swapped; most style guides use only `LEFT`.
- A condition on the **right** table belongs in `ON` for a `LEFT JOIN`. In `WHERE` it removes
  the `NULL`-extended rows and silently turns the join into an inner join:

```sql
-- PostgreSQL: every user with their paid order count, including users with none
SELECT u.user_id, COUNT(o.order_id) AS paid_orders
FROM   users u
LEFT JOIN orders o
       ON o.user_id = u.user_id
      AND o.status  = 'paid'          -- in ON: keeps users with no paid orders
GROUP  BY u.user_id;
-- WHERE o.status = 'paid' here would drop every user without a paid order
```

### 2-2) Semi-joins and anti-joins

**`EXISTS` asks "is there at least one match?" and returns each left row once; a plain join
returns one row per match and can duplicate the left side.**

```sql
-- PostgreSQL: users who placed at least one order (semi-join, no duplicates)
SELECT u.*
FROM   users u
WHERE  EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.user_id);

-- users who never ordered (anti-join) — three equivalent forms
SELECT u.* FROM users u
WHERE  NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.user_id);

SELECT u.* FROM users u
LEFT JOIN orders o ON o.user_id = u.user_id
WHERE  o.user_id IS NULL;

SELECT user_id FROM users
EXCEPT
SELECT user_id FROM orders;
```

- `NOT EXISTS` is the safe default: it is correct when the subquery contains `NULL`s, which
  `NOT IN` is not (see [3-2](#3-2-the-not-in-trap-is-distinct-from-coalesce)).
- Modern optimisers plan `EXISTS`, `IN` and the `LEFT JOIN … IS NULL` form as the same
  semi/anti-join, so pick the one that reads best and is `NULL`-safe.
- Spark SQL also spells them directly: `LEFT SEMI JOIN` and `LEFT ANTI JOIN`.

### 2-3) Self, cross and non-equi joins

**A self-join compares rows of one table with each other; a non-equi join matches on a range
or inequality instead of `=`.**

```sql
-- PostgreSQL: self-join — each employee next to their manager
SELECT e.name AS employee, m.name AS manager
FROM   employees e
LEFT JOIN employees m ON m.emp_id = e.manager_id;

-- employees paid more than their manager
SELECT e.name, e.salary, m.salary AS manager_salary
FROM   employees e
JOIN   employees m ON m.emp_id = e.manager_id
WHERE  e.salary > m.salary;
```

```sql
-- PostgreSQL: non-equi (range) join — tag each order with the price tier it falls in
WITH tiers (tier, low, high) AS (
  VALUES ('small', 0, 50), ('medium', 50, 200), ('large', 200, 1000000)
)
SELECT o.order_id, o.amount, t.tier
FROM   orders o
JOIN   tiers t ON o.amount >= t.low AND o.amount < t.high;
```

- `CROSS JOIN` is intentional in two places: building a grid (every date × every country) and
  replicating a small table (salting a skewed key). Anywhere else, a missing `ON` is a bug.
- Non-equi joins cannot use a hash join; on large tables they fall back to nested loops (or a
  broadcast nested loop in Spark), so keep one side small or add an equality key.

### 2-4) Fan-out: joins that multiply rows

**Joining to a table with several rows per key multiplies the rows, so a `SUM` over the
result double-counts. Check the grain of both sides before every join.**

```sql
-- PostgreSQL — WRONG: each order is repeated once per event of the same user
SELECT u.user_id, SUM(o.amount) AS revenue, COUNT(e.event_ts) AS events
FROM   users u
JOIN   orders o ON o.user_id = u.user_id
JOIN   events e ON e.user_id = u.user_id
GROUP  BY u.user_id;

-- RIGHT: aggregate each side to the user grain first, then join one row to one row
WITH rev AS (SELECT user_id, SUM(amount) AS revenue FROM orders GROUP BY user_id),
     ev  AS (SELECT user_id, COUNT(*)    AS events  FROM events GROUP BY user_id)
SELECT u.user_id, COALESCE(r.revenue, 0) AS revenue, COALESCE(v.events, 0) AS events
FROM   users u
LEFT JOIN rev r ON r.user_id = u.user_id
LEFT JOIN ev  v ON v.user_id = u.user_id;
```

A quick self-check in an interview: count the rows before and after the join. If a
many-to-one join changed the row count of the "many" side, a key is not unique.

### 2-5) Join algorithms

**The engine chooses how to execute a join: nested loop for small or indexed inputs, hash
join for large equi-joins, sort-merge when inputs are sorted or too big for memory.**

| Algorithm | How | Needs | Cost |
|---|---|---|---|
| **Nested loop** | for each outer row, scan (or index-probe) the inner side | nothing — works for any condition | O(n·m); O(n·log m) with an index on the inner side |
| **Hash join** | build a hash table on the smaller side, probe it with the larger | an equality condition; build side fits in memory (or spills) | O(n + m) |
| **Sort-merge join** | sort both sides on the key, walk them together | an equality condition; sortable keys | O(n log n + m log m), or linear if already sorted |
| **Broadcast hash join** (distributed) | copy the small side to every worker, hash-join locally | a small side | no shuffle of the large side |
| **Shuffle hash / sort-merge** (distributed) | repartition both sides by the key across the cluster | equality condition | network shuffle of both sides |

In PostgreSQL, `EXPLAIN` shows `Nested Loop`, `Hash Join` or `Merge Join`; in Spark the
physical plan shows `BroadcastHashJoin` or `SortMergeJoin` (see [`./spark.md`](./spark.md)).

---

## 3) NULL Semantics ⭐⭐⭐⭐

### 3-1) Three-valued logic

**`NULL` means "unknown", so any comparison with it is `UNKNOWN`, not true or false — and
`WHERE` keeps only rows where the condition is `TRUE`.**

| Expression | Result |
|---|---|
| `NULL = NULL` | `UNKNOWN` |
| `NULL <> 1` | `UNKNOWN` |
| `NULL AND FALSE` | `FALSE` |
| `NULL AND TRUE` | `UNKNOWN` |
| `NULL OR TRUE` | `TRUE` |
| `NOT NULL` (the boolean) | `UNKNOWN` |
| `NULL + 1`, `'a' \|\| NULL` | `NULL` (PostgreSQL; `CONCAT` skips NULLs) |

- Test with `IS NULL` / `IS NOT NULL`, never `= NULL`.
- `WHERE status <> 'paid'` also drops rows whose `status` is `NULL`. If those rows should be
  kept, say so: `WHERE status IS DISTINCT FROM 'paid'`.

### 3-2) The `NOT IN` trap, `IS DISTINCT FROM`, `COALESCE`

**`x NOT IN (subquery)` returns no rows at all if the subquery yields a single `NULL`,
because `x <> NULL` is `UNKNOWN` for every `x`.**

```sql
-- PostgreSQL
SELECT 1 WHERE 3 NOT IN (1, 2);          -- one row
SELECT 1 WHERE 3 NOT IN (1, 2, NULL);    -- zero rows: 3 <> NULL is UNKNOWN

-- safe anti-join: NOT EXISTS ignores NULLs in the subquery
SELECT e.name
FROM   employees e
WHERE  NOT EXISTS (SELECT 1 FROM employees r WHERE r.manager_id = e.emp_id);  -- non-managers
-- NOT IN (SELECT manager_id FROM employees) would return nothing: the CEO's manager_id is NULL
```

| Tool | Does |
|---|---|
| `a IS DISTINCT FROM b` | `NULL`-safe `<>`: two `NULL`s are equal, `NULL` vs a value is different (Snowflake / Spark also `<=>` or `EQUAL_NULL`) |
| `COALESCE(a, b, c)` | the first non-`NULL` argument |
| `NULLIF(a, b)` | `NULL` if `a = b`, else `a` — `x / NULLIF(y, 0)` avoids division by zero |

`IS DISTINCT FROM` is the comparison to use in change detection — an SCD2 update or a `MERGE`
that decides whether a row changed — because `old <> new` misses a change to or from `NULL`.

### 3-3) NULLs in aggregates, grouping and sorting

- `COUNT(*)` counts rows; `COUNT(col)` counts non-`NULL` values; `COUNT(DISTINCT col)` ignores `NULL`.
- `SUM`, `AVG`, `MIN`, `MAX` skip `NULL`s, so `AVG(col)` is the mean of the known values, not
  `SUM / COUNT(*)`. `SUM` over only `NULL`s (or no rows) is `NULL` — wrap it in `COALESCE(…, 0)`.
- `GROUP BY` puts all `NULL`s into **one** group, even though `NULL = NULL` is unknown.
- Sort placement differs by engine — PostgreSQL and Snowflake put `NULL`s last in ascending
  order, BigQuery and Spark put them first. Write `NULLS FIRST` / `NULLS LAST` when it matters.
- A `UNIQUE` constraint allows many `NULL`s in PostgreSQL (unless `NULLS NOT DISTINCT`, PostgreSQL 15+).

---

## 4) Aggregation ⭐⭐⭐⭐

### 4-1) Conditional aggregation and `FILTER`

**Conditional aggregation computes several metrics in one pass: put the condition inside the
aggregate instead of in `WHERE`.**

```sql
-- PostgreSQL: per-country order metrics in a single scan
SELECT u.country,
       COUNT(*)                                          AS orders,
       COUNT(*)    FILTER (WHERE o.status = 'refunded')  AS refunds,
       SUM(o.amount) FILTER (WHERE o.status = 'paid')    AS paid_revenue,
       -- portable spelling of the same thing (BigQuery, Snowflake, Spark):
       SUM(CASE WHEN o.status = 'paid' THEN o.amount END) AS paid_revenue_case,
       ROUND(AVG(CASE WHEN o.status = 'refunded' THEN 1.0 ELSE 0 END), 3) AS refund_rate
FROM   orders o
JOIN   users  u ON u.user_id = o.user_id
GROUP  BY u.country;
```

- `FILTER (WHERE …)` is standard SQL, supported by PostgreSQL, SQLite and DuckDB; BigQuery,
  Snowflake use the `CASE WHEN` form (Spark SQL accepts both).
- `CASE WHEN … THEN 1 ELSE 0 END` averaged gives a rate; leaving out `ELSE` gives `NULL`,
  which `SUM` and `COUNT` skip — both are useful, mixing them up is a classic bug.
- Every non-aggregated column in `SELECT` must be in `GROUP BY` (PostgreSQL relaxes this for
  columns functionally dependent on a grouped primary key).

### 4-2) `GROUPING SETS`, `ROLLUP`, `CUBE`

**These produce several levels of aggregation in one query — the subtotal and grand-total
rows a report needs — instead of a `UNION ALL` of several `GROUP BY`s.**

| Clause | Groups produced for `(a, b)` |
|---|---|
| `GROUPING SETS ((a, b), (a), ())` | exactly the listed sets |
| `ROLLUP (a, b)` | `(a, b)`, `(a)`, `()` — a hierarchy: detail, subtotal, total |
| `CUBE (a, b)` | `(a, b)`, `(a)`, `(b)`, `()` — every combination |

```sql
-- PostgreSQL: revenue by country and month, with per-country subtotals and a grand total
SELECT u.country,
       DATE_TRUNC('month', o.order_date)::date AS month,
       SUM(o.amount)                          AS revenue,
       GROUPING(u.country, DATE_TRUNC('month', o.order_date)) AS grouping_level
FROM   orders o
JOIN   users  u ON u.user_id = o.user_id
GROUP  BY ROLLUP (u.country, DATE_TRUNC('month', o.order_date))
ORDER  BY u.country NULLS LAST, month NULLS LAST;
```

`GROUPING()` returns a bitmask telling a subtotal `NULL` (the column was rolled up) apart from
a real `NULL` in the data — 0 for a detail row, 1 for a country subtotal, 3 for the grand total
here.

### 4-3) `COUNT(DISTINCT)` and approximate aggregates

**Exact `COUNT(DISTINCT)` must remember every distinct value, which is memory- and
shuffle-heavy at scale; approximate functions use a fixed-size sketch (HyperLogLog) and trade
a small, bounded error for that.**

| Need | PostgreSQL | BigQuery | Snowflake | Spark SQL |
|---|---|---|---|---|
| distinct count | `COUNT(DISTINCT x)` (approximate via the `hll` extension) | `APPROX_COUNT_DISTINCT(x)` | `APPROX_COUNT_DISTINCT(x)` | `approx_count_distinct(x)` |
| percentile | `PERCENTILE_CONT(p) WITHIN GROUP (ORDER BY x)` (exact) | `APPROX_QUANTILES(x, 100)[OFFSET(p)]` | `APPROX_PERCENTILE(x, p)` | `percentile_approx(x, p)` |
| top values | — | `APPROX_TOP_COUNT(x, k)` | `APPROX_TOP_K(x, k)` | — |

- Sketches can be **merged**: store a daily HLL sketch per segment and union them for a weekly
  distinct count, which exact counts cannot do (`DAU` does not add up to `WAU`).
- Use exact counts for anything billed or reconciled; approximate for dashboards and exploration.

---

## 5) Window Functions ⭐⭐⭐⭐⭐

### 5-1) Anatomy of a window

**A window function computes a value for each row from a set of related rows — its
partition — without collapsing them. `GROUP BY` returns one row per group; a window keeps
every row.**

```text
function(args) OVER (
    PARTITION BY  user_id                -- which rows are related   (optional: default = all rows)
    ORDER BY      order_date             -- order inside the partition
    ROWS BETWEEN  2 PRECEDING            -- the frame: which of those rows feed this row
              AND CURRENT ROW
)
```

| Family | Functions | Frame used? |
|---|---|---|
| Ranking | `ROW_NUMBER`, `RANK`, `DENSE_RANK`, `PERCENT_RANK`, `CUME_DIST`, `NTILE(n)` | no |
| Offset | `LAG(x, n, default)`, `LEAD(x, n, default)` | no |
| Value | `FIRST_VALUE`, `LAST_VALUE`, `NTH_VALUE` | **yes** |
| Aggregate | `SUM`, `AVG`, `COUNT`, `MIN`, `MAX` … `OVER (…)` | **yes** |

A named window avoids repeating the definition: `… OVER w … WINDOW w AS (PARTITION BY user_id
ORDER BY order_date)` (PostgreSQL, BigQuery, Spark, DuckDB).

### 5-2) Ranking: `ROW_NUMBER` vs `RANK` vs `DENSE_RANK`

**They differ only on ties: `ROW_NUMBER` breaks them arbitrarily, `RANK` gives ties the same
rank and skips the next, `DENSE_RANK` gives ties the same rank and does not skip.**

```text
salary   ROW_NUMBER   RANK   DENSE_RANK
 900         1          1        1
 800         2          2        2
 800         3          2        2
 700         4          4        3
```

- "Top 3 salaries per department, ties included" → `DENSE_RANK() <= 3`.
- "Exactly one row per key" (dedupe) → `ROW_NUMBER() = 1`, **with a tie-breaker in `ORDER BY`**
  (`ORDER BY updated_at DESC, ingest_id DESC`), or the chosen row changes between runs.
- `NTILE(4)` splits rows into four buckets of near-equal size (quartiles by count, not by value).

### 5-3) `LAG` and `LEAD`

**`LAG` reads a column from an earlier row of the partition and `LEAD` from a later one —
the tool for "compared with the previous" questions.**

```sql
-- PostgreSQL: days since each user's previous order, and the change in amount
SELECT user_id, order_date, amount,
       order_date - LAG(order_date) OVER w       AS days_since_prev,   -- DATE - DATE = INT
       amount     - LAG(amount, 1, 0) OVER w     AS diff_vs_prev,      -- default 0 for the first
       LEAD(order_date) OVER w                   AS next_order_date
FROM   orders
WINDOW w AS (PARTITION BY user_id ORDER BY order_date, order_id);
```

`LAG(x, n)` counts **rows**, not time: if a month is missing, `LAG(revenue, 12)` is not "the
same month last year". Fill the calendar first (see [7-4](#7-4-month-over-month-and-year-over-year-growth)).

### 5-4) Frames: `ROWS` vs `RANGE` vs `GROUPS` ⭐⭐⭐⭐⭐

**The frame decides which rows of the partition feed an aggregate window: `ROWS` counts
physical rows, `RANGE` uses the value of the `ORDER BY` column, `GROUPS` counts peer groups
of tied values.**

| Frame | `2 PRECEDING` means | Ties (peers) |
|---|---|---|
| `ROWS` | the two previous rows | split — each row is separate |
| `RANGE` | values within 2 of the current value (needs one numeric / date `ORDER BY`) | included together |
| `GROUPS` | the two previous groups of equal values (PostgreSQL 11+, SQLite, DuckDB) | included together |

The defaults, which are the source of most window bugs:

- **No `ORDER BY`** → the frame is the whole partition: `SUM(x) OVER (PARTITION BY k)` is the group total on every row.
- **`ORDER BY` and no frame** → `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`. A running
  sum then **adds all rows tied on the order key at once**, and `LAST_VALUE(x)` returns the
  current row's peer group, not the partition's last row.

```sql
-- PostgreSQL: three different "last 7" windows on daily revenue
WITH daily AS (
  SELECT order_date, SUM(amount) AS revenue
  FROM   orders GROUP BY order_date
)
SELECT order_date, revenue,
       SUM(revenue) OVER (ORDER BY order_date
                          ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)   AS running_total,
       AVG(revenue) OVER (ORDER BY order_date
                          ROWS BETWEEN 6 PRECEDING AND CURRENT ROW)           AS avg_last_7_rows,
       AVG(revenue) OVER (ORDER BY order_date
                          RANGE BETWEEN INTERVAL '6 days' PRECEDING AND CURRENT ROW) AS avg_last_7_days,
       LAST_VALUE(revenue) OVER (ORDER BY order_date
                          ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS last_day_revenue
FROM   daily;
```

`avg_last_7_rows` and `avg_last_7_days` differ as soon as a day has no orders: the row frame
reaches back past the gap to seven rows, the range frame covers seven calendar days. A
"7-day moving average" means the second — or a date spine plus the first.

### 5-5) Filtering on a window: CTE or `QUALIFY`

**You cannot put a window function in `WHERE`; compute it in a CTE and filter outside, or
use `QUALIFY` where the engine has it.**

```sql
-- PostgreSQL (any engine): the highest-paid employee in each department
WITH ranked AS (
  SELECT e.*, ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC, emp_id) AS rn
  FROM   employees e
)
SELECT emp_id, name, dept, salary FROM ranked WHERE rn = 1;
```

```sql
-- Snowflake (also BigQuery, Databricks, DuckDB): the same with QUALIFY
SELECT emp_id, name, dept, salary
FROM   employees
QUALIFY ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC, emp_id) = 1;
```

PostgreSQL also has `SELECT DISTINCT ON (dept) … ORDER BY dept, salary DESC` for one row per
key, but it is PostgreSQL-only and does not generalise to top-N.

---

## 6) CTEs, Subqueries and Set Operations ⭐⭐⭐⭐

### 6-1) CTE vs subquery vs temporary table

**A CTE (`WITH`) is a named subquery: it makes a long query readable step by step. Whether it
is computed once or inlined is the optimiser's choice, not a guarantee.**

| | CTE | Subquery | Temp table |
|---|---|---|---|
| Readability | best — named, top-down steps | nested, harder to read | separate statements |
| Reuse within one query | yes, by name | must repeat | yes |
| Materialised? | engine-dependent | inlined | yes, physically |
| Statistics / indexes | no | no | yes (can `ANALYZE`, add an index) |

- PostgreSQL 12+ inlines a non-recursive CTE that is referenced once; `WITH x AS MATERIALIZED
  (…)` forces a single evaluation, `NOT MATERIALIZED` forces inlining.
- BigQuery and Snowflake may evaluate a CTE referenced twice **twice**. If a CTE is expensive
  and reused, a temp table can be cheaper.
- In an interview, write CTEs: one per step (filter → aggregate → rank → filter) makes the
  logic checkable aloud.

### 6-2) Correlated subqueries vs joins

**A correlated subquery references the outer row, so it is logically evaluated once per outer
row; most engines rewrite simple ones into joins, but a complex one can stay row by row.**

```sql
-- PostgreSQL: employees earning above their department's average
-- correlated form
SELECT e.name, e.dept, e.salary
FROM   employees e
WHERE  e.salary > (SELECT AVG(x.salary) FROM employees x WHERE x.dept = e.dept);

-- window form: one pass, no self-reference
SELECT name, dept, salary
FROM  (SELECT e.*, AVG(salary) OVER (PARTITION BY dept) AS dept_avg FROM employees e) t
WHERE  salary > dept_avg;
```

The window form scans the table once and is the answer to offer when asked "can you do it
without the subquery?".

### 6-3) Recursive CTEs: hierarchies and sequences

**A recursive CTE has an anchor query and a recursive query that references the CTE itself;
it repeats until the recursive part returns no new rows — the standard way to walk a tree
stored as parent pointers.**

```sql
-- PostgreSQL: the full reporting chain under the CEO, with depth and path
WITH RECURSIVE org AS (
  SELECT emp_id, name, manager_id, 0 AS depth, name::text AS path
  FROM   employees
  WHERE  manager_id IS NULL                              -- anchor: the root
  UNION ALL
  SELECT e.emp_id, e.name, e.manager_id, o.depth + 1, o.path || ' > ' || e.name
  FROM   employees e
  JOIN   org o ON e.manager_id = o.emp_id                -- recursive step: children
)
SELECT * FROM org ORDER BY path;
```

- Guard against cycles in dirty data: stop at a maximum depth (`WHERE o.depth < 20`), or use
  PostgreSQL 14's `CYCLE emp_id SET is_cycle USING path_ids`.
- `UNION` instead of `UNION ALL` deduplicates each step, which also stops some cycles but costs a sort.
- BigQuery and Snowflake support `WITH RECURSIVE`; Apache Spark added recursive CTEs only in
  the 4.x line — on older Spark, iterate in application code instead.

### 6-4) Set operations

**`UNION ALL` concatenates; `UNION` concatenates and removes duplicates, which costs a sort or
hash over the whole result — use `UNION ALL` unless you need the dedupe.**

| Operator | Returns |
|---|---|
| `UNION ALL` | every row of both inputs |
| `UNION` (`UNION DISTINCT`) | distinct rows of both |
| `INTERSECT` | distinct rows present in both |
| `EXCEPT` (`MINUS` in Oracle / Snowflake) | distinct rows of the first not in the second |

- Columns are matched **by position**, not by name, and must have compatible types.
- BigQuery requires the keyword: `UNION ALL` or `UNION DISTINCT`; a bare `UNION` is an error.
- Set operations treat two `NULL`s as equal, unlike `=` — so `EXCEPT` is a `NULL`-safe anti-join on whole rows.

---

## 7) The Interview Patterns ⭐⭐⭐⭐⭐

### 7-1) Top-N per group

**Rank inside each group with a window, then keep the ranks you want; pick the ranking
function by how ties should behave.**

```sql
-- PostgreSQL: top 2 users by paid revenue in each country, ties included
WITH user_rev AS (
  SELECT u.country, o.user_id, SUM(o.amount) AS revenue
  FROM   orders o
  JOIN   users  u ON u.user_id = o.user_id
  WHERE  o.status = 'paid'
  GROUP  BY u.country, o.user_id
), ranked AS (
  SELECT *, DENSE_RANK() OVER (PARTITION BY country ORDER BY revenue DESC) AS rnk
  FROM   user_rev
)
SELECT country, user_id, revenue, rnk
FROM   ranked
WHERE  rnk <= 2
ORDER  BY country, rnk;
```

The "second highest salary" classic is the same pattern with no partition — and it must return
`NULL`, not an empty result, when there is no second value:

```sql
-- PostgreSQL: second-highest distinct salary, NULL if there is none
SELECT (SELECT DISTINCT salary
        FROM   employees
        ORDER  BY salary DESC
        OFFSET 1 LIMIT 1) AS second_highest_salary;
```

### 7-2) Deduplicate: keep the latest row per key

**Number the rows of each key newest first and keep number 1 — the standard cleanup for CDC
and event tables that receive the same record more than once.**

```sql
-- PostgreSQL: a change log that received order 1 twice
CREATE TEMP TABLE order_changes (order_id INT, status TEXT, updated_at TIMESTAMP);
INSERT INTO order_changes VALUES
  (1, 'paid',     TIMESTAMP '2026-11-01 10:00'),
  (1, 'refunded', TIMESTAMP '2026-11-03 09:00'),
  (2, 'paid',     TIMESTAMP '2026-11-02 12:00');

-- latest status per order
WITH ranked AS (
  SELECT *, ROW_NUMBER() OVER (PARTITION BY order_id ORDER BY updated_at DESC) AS rn
  FROM   order_changes
)
SELECT order_id, status, updated_at FROM ranked WHERE rn = 1;

-- just find the duplicated keys
SELECT order_id, COUNT(*) FROM order_changes GROUP BY order_id HAVING COUNT(*) > 1;
```

- `SELECT DISTINCT` only removes rows identical in **every** column; it is not a dedupe by key.
- **Make the order deterministic.** Two rows with the same `updated_at` let `ROW_NUMBER` pick
  either one, and a rerun may pick the other. Add a tie-breaker that really orders the
  changes — a source sequence or log position (LSN) if there is one:
  `ORDER BY updated_at DESC, source_seq DESC`.
- **`MAX(updated_at)` joined back is the wrong answer.** `JOIN (SELECT order_id,
  MAX(updated_at) …)` returns *both* rows when two share the latest timestamp, so the
  "one row per key" result has duplicates in it.
- **Say what a duplicate is before removing one.** `paid` then `refunded` for order 1 are two
  real changes, not a duplicate; the same change delivered twice is. The key is the table's
  grain — `order_id` for a current-state table, `(order_id, version)` or `event_id` for a
  change log.

### 7-3) Running totals and moving averages

```sql
-- PostgreSQL: per-user cumulative spend and a 3-order moving average
SELECT user_id, order_date, amount,
       SUM(amount) OVER (PARTITION BY user_id ORDER BY order_date, order_id
                         ROWS UNBOUNDED PRECEDING)                   AS cumulative_spend,
       ROUND(AVG(amount) OVER (PARTITION BY user_id ORDER BY order_date, order_id
                         ROWS BETWEEN 2 PRECEDING AND CURRENT ROW), 2) AS moving_avg_3,
       ROUND(100.0 * amount / SUM(amount) OVER (PARTITION BY user_id), 1) AS pct_of_user_total
FROM   orders
WHERE  status = 'paid';
```

`ROWS UNBOUNDED PRECEDING` is shorthand for `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`;
writing `ROWS` explicitly (and a unique tie-breaker in `ORDER BY`) avoids the `RANGE` default
summing tied rows together.

### 7-4) Month-over-month and year-over-year growth

**Aggregate to the month first, then compare each month with the previous one with `LAG` —
and make sure every month exists, or `LAG` compares with the wrong month.**

```sql
-- PostgreSQL: MoM and YoY revenue growth on a gap-free month spine
WITH months AS (
  SELECT generate_series(DATE '2025-01-01', DATE '2026-12-01', INTERVAL '1 month')::date AS month
), monthly AS (
  SELECT DATE_TRUNC('month', order_date)::date AS month, SUM(amount) AS revenue
  FROM   orders
  WHERE  status = 'paid'
  GROUP  BY 1
), filled AS (
  SELECT m.month, COALESCE(r.revenue, 0) AS revenue
  FROM   months m
  LEFT JOIN monthly r ON r.month = m.month
)
SELECT month, revenue,
       LAG(revenue)     OVER (ORDER BY month)                     AS prev_month,
       ROUND(100.0 * (revenue - LAG(revenue) OVER (ORDER BY month))
             / NULLIF(LAG(revenue) OVER (ORDER BY month), 0), 1)  AS mom_pct,
       ROUND(100.0 * (revenue - LAG(revenue, 12) OVER (ORDER BY month))
             / NULLIF(LAG(revenue, 12) OVER (ORDER BY month), 0), 1) AS yoy_pct
FROM   filled
ORDER  BY month;
```

`NULLIF(…, 0)` turns a zero base into `NULL` growth instead of a division-by-zero error — say
out loud that growth from zero is undefined, not infinite.

### 7-5) Gaps and islands: consecutive streaks ⭐⭐⭐⭐⭐

**Within a run of consecutive dates, `date − ROW_NUMBER()` is constant, so it labels each run
(island); group by that label to get each streak's start, end and length.**

```text
login_date   row_number   login_date - row_number
2026-11-01       1            2026-10-31    ┐ island A
2026-11-02       2            2026-10-31    │
2026-11-03       3            2026-10-31    ┘
2026-11-05       4            2026-11-01    ┐ island B
2026-11-06       5            2026-11-01    ┘
```

```sql
-- PostgreSQL: users with a login streak of at least 3 days
WITH days AS (
  SELECT DISTINCT user_id, event_ts::date AS d          -- DISTINCT: one row per day
  FROM   events
), grp AS (
  SELECT user_id, d,
         d - CAST(ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY d) AS INT) AS island
  FROM   days
)
SELECT user_id, MIN(d) AS streak_start, MAX(d) AS streak_end, COUNT(*) AS streak_days
FROM   grp
GROUP  BY user_id, island
HAVING COUNT(*) >= 3
ORDER  BY user_id, streak_start;
```

- The `DISTINCT` matters: two events on one day would number the same date twice and break the identity.
- The same trick finds gaps (missing ids, missing days) and merges overlapping ranges, with
  `LAG` flagging where a new island starts instead of the subtraction.

### 7-6) Comparing with neighbouring rows

**When a condition spans consecutive rows — "bought again within 7 days", "three increasing
orders in a row" — put the neighbours on the same row with `LAG` / `LEAD` and filter.**

```sql
-- PostgreSQL: repeat purchases within 7 days of the user's previous order
SELECT user_id, order_date, prev_date, order_date - prev_date AS gap_days
FROM  (SELECT user_id, order_date,
              LAG(order_date) OVER (PARTITION BY user_id ORDER BY order_date, order_id) AS prev_date
       FROM   orders) t
WHERE  order_date - prev_date <= 7;

-- three strictly increasing order amounts in a row
SELECT DISTINCT user_id
FROM  (SELECT user_id, amount,
              LAG(amount, 1) OVER w AS prev1,
              LAG(amount, 2) OVER w AS prev2
       FROM   orders
       WINDOW w AS (PARTITION BY user_id ORDER BY order_date, order_id)) t
WHERE  prev2 < prev1 AND prev1 < amount;
```

### 7-7) Sessionisation

**A new session starts when the gap since the user's previous event exceeds a timeout; flag
those starts with `LAG`, then a running `SUM` of the flags numbers the sessions.**

```sql
-- PostgreSQL: 30-minute inactivity sessions
WITH flagged AS (
  SELECT user_id, event_ts, event_name,
         CASE WHEN event_ts - LAG(event_ts) OVER (PARTITION BY user_id ORDER BY event_ts)
                   <= INTERVAL '30 minutes'
              THEN 0 ELSE 1 END AS is_new_session            -- first event: LAG is NULL -> 1
  FROM   events
), numbered AS (
  SELECT *, SUM(is_new_session) OVER (PARTITION BY user_id ORDER BY event_ts
                                      ROWS UNBOUNDED PRECEDING) AS session_no
  FROM   flagged
)
SELECT user_id, session_no,
       MIN(event_ts) AS session_start,
       MAX(event_ts) AS session_end,
       COUNT(*)      AS events
FROM   numbered
GROUP  BY user_id, session_no
ORDER  BY user_id, session_no;
```

Writing the condition as `<= 30 minutes THEN 0 ELSE 1` makes the first event (where `LAG` is
`NULL`) start a session automatically, with no separate `IS NULL` check.

### 7-8) Retention cohorts

**Assign each user a cohort (their signup month), compute how many months after signup each
activity falls, and divide the active users at each offset by the cohort size.**

```sql
-- PostgreSQL: monthly retention by signup cohort
WITH cohort AS (
  SELECT user_id, DATE_TRUNC('month', signup_date)::date AS cohort_month
  FROM   users
), activity AS (
  SELECT DISTINCT user_id, DATE_TRUNC('month', order_date)::date AS active_month
  FROM   orders
), joined AS (
  SELECT c.cohort_month, a.user_id,
         (EXTRACT(YEAR  FROM a.active_month) - EXTRACT(YEAR  FROM c.cohort_month)) * 12
       + (EXTRACT(MONTH FROM a.active_month) - EXTRACT(MONTH FROM c.cohort_month)) AS month_n
  FROM   cohort c
  JOIN   activity a ON a.user_id = c.user_id
), sizes AS (
  SELECT cohort_month, COUNT(*) AS cohort_size FROM cohort GROUP BY cohort_month
)
SELECT j.cohort_month, j.month_n, s.cohort_size,
       COUNT(DISTINCT j.user_id) AS active_users,
       ROUND(100.0 * COUNT(DISTINCT j.user_id) / s.cohort_size, 1) AS retention_pct
FROM   joined j
JOIN   sizes  s ON s.cohort_month = j.cohort_month
WHERE  j.month_n >= 0
GROUP  BY j.cohort_month, j.month_n, s.cohort_size
ORDER  BY j.cohort_month, j.month_n;
```

The cohort size comes from **all** users in the cohort, not from the ones that were active —
otherwise month 0 is always 100% and every later month is inflated.

### 7-9) Funnels

**Find each user's first time reaching each step, then count users who reached every step in
order.**

```sql
-- PostgreSQL: view -> cart -> purchase funnel
WITH steps AS (
  SELECT user_id,
         MIN(event_ts) FILTER (WHERE event_name = 'view')     AS t_view,
         MIN(event_ts) FILTER (WHERE event_name = 'cart')     AS t_cart,
         MIN(event_ts) FILTER (WHERE event_name = 'purchase') AS t_purchase
  FROM   events
  GROUP  BY user_id
)
SELECT COUNT(t_view)                                                AS viewed,
       COUNT(*) FILTER (WHERE t_cart >= t_view)                     AS carted,
       COUNT(*) FILTER (WHERE t_purchase >= t_cart
                          AND t_cart >= t_view)                     AS purchased
FROM   steps;
```

Trade-off to name: using each step's **first** time is simple but undercounts a user whose
first cart came before their first view and who later went through in order. A strict version
walks events in order per user (a `LAG`-based or session-based funnel) — say which one the
business wants.

### 7-10) Median and percentiles

**`PERCENTILE_CONT` interpolates between the two middle values; `PERCENTILE_DISC` returns an
actual value from the data.**

```sql
-- PostgreSQL: median and p90 order amount per country
SELECT u.country,
       PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY o.amount) AS median_amount,
       PERCENTILE_DISC(0.5) WITHIN GROUP (ORDER BY o.amount) AS median_actual_value,
       PERCENTILE_CONT(0.9) WITHIN GROUP (ORDER BY o.amount) AS p90_amount
FROM   orders o
JOIN   users  u ON u.user_id = o.user_id
GROUP  BY u.country;
```

- The median is robust to outliers, which is why interviewers ask for it instead of `AVG`.
- In BigQuery `PERCENTILE_CONT(x, 0.5) OVER (PARTITION BY …)` is a window function, not an
  aggregate; at scale use `APPROX_QUANTILES`.

### 7-11) Pivot and unpivot

**Pivot turns row values into columns with conditional aggregation; unpivot turns columns into
rows by joining each row to a list of its column values.**

```sql
-- PostgreSQL: pivot — one row per user, one column per status
SELECT user_id,
       COUNT(*) FILTER (WHERE status = 'paid')      AS paid,
       COUNT(*) FILTER (WHERE status = 'refunded')  AS refunded,
       COUNT(*) FILTER (WHERE status = 'cancelled') AS cancelled
FROM   orders
GROUP  BY user_id;

-- unpivot — columns back into (metric, value) rows
WITH wide (user_id, paid, refunded) AS (VALUES (1, 3, 1), (2, 5, 0))
SELECT w.user_id, v.metric, v.value
FROM   wide w
CROSS JOIN LATERAL (VALUES ('paid', w.paid), ('refunded', w.refunded)) AS v (metric, value);
```

Snowflake, BigQuery, Spark and DuckDB also have `PIVOT` / `UNPIVOT` operators, but they need the
pivoted values listed in advance — conditional aggregation is portable and what to write on a
whiteboard.

### 7-12) Histograms and distributions

**A distribution question is two aggregations stacked: first per entity, then count entities
per value or bucket.**

```sql
-- PostgreSQL: how many users placed 1, 2, 3 ... orders
SELECT order_count, COUNT(*) AS users
FROM  (SELECT user_id, COUNT(*) AS order_count FROM orders GROUP BY user_id) per_user
GROUP  BY order_count
ORDER  BY order_count;

-- order amounts in 50-wide buckets
SELECT FLOOR(amount / 50) * 50 AS bucket_start, COUNT(*) AS orders
FROM   orders
GROUP  BY 1
ORDER  BY 1;
```

Users with **zero** orders are missing from the first query; if the question includes them,
start from `users` with a `LEFT JOIN`. PostgreSQL also has `width_bucket(amount, 0, 1000, 20)`.

### 7-13) "Did X but not Y"

**Either an anti-join between two sets, or one pass that counts both conditions per entity and
keeps `X > 0 AND Y = 0`.**

```sql
-- PostgreSQL: users who added to cart but never purchased
SELECT user_id
FROM   events
GROUP  BY user_id
HAVING COUNT(*) FILTER (WHERE event_name = 'cart')     > 0
   AND COUNT(*) FILTER (WHERE event_name = 'purchase') = 0;

-- customers who ordered in October 2026 but not in November 2026
SELECT DISTINCT o.user_id
FROM   orders o
WHERE  o.order_date >= DATE '2026-10-01' AND o.order_date < DATE '2026-11-01'
  AND  NOT EXISTS (SELECT 1 FROM orders n
                   WHERE  n.user_id = o.user_id
                     AND  n.order_date >= DATE '2026-11-01' AND n.order_date < DATE '2026-12-01');
```

The single-pass `HAVING` form reads the table once and extends naturally to "did X, Y and Z
but not W".

### 7-14) Rebuild validity intervals from a change log ⭐⭐⭐⭐

**Order each key's changes by when they took effect, and let `LEAD` hand every version the
start of the next one as its end — so a change that arrives late lands in the right place
the next time the history is rebuilt.**

This is the query behind an SCD Type 2 dimension and behind "late-arriving CDC": the
Tokyo change below happened on 1 Feb but reached the pipeline on 5 Mar, after the
Kaohsiung change.

```sql
-- PostgreSQL: rebuild each user's country history from a change log
CREATE TEMP TABLE user_changes (
  user_id     INT,
  country     TEXT,
  valid_from  DATE,       -- when the change took effect (event time)
  arrived_at  DATE,       -- when the pipeline received it
  source_seq  INT         -- the source's own order, e.g. an LSN
);
INSERT INTO user_changes VALUES
  (1, 'TW', DATE '2026-01-01', DATE '2026-01-01', 101),
  (1, 'KR', DATE '2026-03-01', DATE '2026-03-01', 103),
  (1, 'JP', DATE '2026-02-01', DATE '2026-03-05', 102),   -- arrived late
  (2, 'US', DATE '2026-01-15', DATE '2026-01-15', 201);

SELECT user_id, country,
       valid_from,
       COALESCE(LEAD(valid_from) OVER w, DATE '9999-12-31') AS valid_to,   -- exclusive
       LEAD(valid_from) OVER w IS NULL                       AS is_current
FROM   user_changes
WINDOW w AS (PARTITION BY user_id ORDER BY valid_from, source_seq)
ORDER  BY user_id, valid_from;
```

```text
user_id  country  valid_from  valid_to    is_current
1        TW       2026-01-01  2026-02-01  false
1        JP       2026-02-01  2026-03-01  false
1        KR       2026-03-01  9999-12-31  true
2        US       2026-01-15  9999-12-31  true
```

- **Order by event time, never by arrival time.** Ordered by `arrived_at`, JP would be the
  current country.
- **Half-open intervals** — `valid_from <= t < valid_to` — so a timestamp on a boundary
  belongs to exactly one version. A point-in-time join is then
  `ON f.user_id = d.user_id AND f.order_date >= d.valid_from AND f.order_date < d.valid_to`.
- Two checks that should return no rows on any SCD2 table:

```sql
-- PostgreSQL: more than one current version per key
SELECT user_id FROM dim_user_history WHERE is_current GROUP BY user_id HAVING COUNT(*) > 1;

-- overlapping versions of the same key: a version that starts before an earlier one ends
SELECT user_id, valid_from, valid_to, prev_max_to
FROM  (SELECT *,
              MAX(valid_to) OVER (PARTITION BY user_id ORDER BY valid_from, valid_to
                                  ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING) AS prev_max_to
       FROM   dim_user_history) t
WHERE  valid_from < prev_max_to;
```

The overlap check compares each version with the latest end among the versions sorted before
it. That catches two versions with the **same** `valid_from`, which a self-join on
`a.valid_from < b.valid_from` skips, and it is one sort rather than a pairwise join.

Rebuilding a key's whole history is the simplest correct answer to late data; doing it only
for the keys that received a late change is the optimisation. How the dimension is loaded
with `MERGE` is [data_modeling.md](./data_modeling.md)'s topic.

### 7-15) Peak concurrency: overlapping intervals

**Turn each interval into a `+1` at its start and a `−1` at its end, sort the points, and a
running sum is the number open at each moment; its maximum is the peak.**

The classic form is "most users online at once", "rooms needed for these meetings" or
"maximum concurrent sessions".

```sql
-- PostgreSQL: the peak number of concurrent sessions, and when it happened
CREATE TEMP TABLE sessions (session_id INT, start_ts TIMESTAMP, end_ts TIMESTAMP);
INSERT INTO sessions VALUES
  (1, TIMESTAMP '2026-11-01 10:00', TIMESTAMP '2026-11-01 10:30'),
  (2, TIMESTAMP '2026-11-01 10:10', TIMESTAMP '2026-11-01 10:20'),
  (3, TIMESTAMP '2026-11-01 10:15', TIMESTAMP '2026-11-01 10:45'),
  (4, TIMESTAMP '2026-11-01 10:30', TIMESTAMP '2026-11-01 11:00');

WITH points AS (
  SELECT start_ts AS ts, +1 AS delta FROM sessions
  UNION ALL
  SELECT end_ts   AS ts, -1 AS delta FROM sessions
), running AS (
  SELECT ts,
         SUM(delta) OVER (ORDER BY ts, delta          -- -1 before +1 at the same instant
                          ROWS UNBOUNDED PRECEDING) AS open_sessions
  FROM   points
)
SELECT ts, open_sessions
FROM   running
ORDER  BY open_sessions DESC, ts
LIMIT  1;
-- 2026-11-01 10:15, 3 (sessions 1, 2 and 3)
```

- **Ties decide the answer.** Session 1 ends at 10:30 as session 4 starts. Sorting `delta`
  ascending puts the `−1` first, so they do not count as overlapping — right for half-open
  intervals. Sort `+1` first if an end time is inclusive.
- `ROWS`, not the default `RANGE`, so each point adds to the sum on its own row.
- One sort over 2n points: O(n log n), and it works the same way in any warehouse. A
  self-join that compares every pair is O(n²).

### 7-16) Data quality checks as SQL assertions ⭐⭐⭐⭐

**Write each check as a query that returns the bad rows, or a count that must be zero, and
let the pipeline fail when it is not.**

```sql
-- PostgreSQL: one row per check, so the result can be stored and alerted on
SELECT 'null_order_id' AS check_name, COUNT(*) AS failures
FROM   orders WHERE order_id IS NULL
UNION ALL
SELECT 'duplicate_order_id', COUNT(*)
FROM  (SELECT order_id FROM orders GROUP BY order_id HAVING COUNT(*) > 1) d
UNION ALL
SELECT 'negative_amount', COUNT(*)
FROM   orders WHERE amount < 0
UNION ALL
SELECT 'unknown_status', COUNT(*)
FROM   orders WHERE status NOT IN ('paid', 'refunded', 'cancelled')
UNION ALL
SELECT 'orphan_user_id', COUNT(*)                              -- referential integrity
FROM   orders o WHERE NOT EXISTS (SELECT 1 FROM users u WHERE u.user_id = o.user_id);
```

Reconciliation compares the load with its source **for the same slice**, so a mismatch
points at one day rather than at the whole table:

```sql
-- PostgreSQL: row count and revenue per day, source vs target
WITH src AS (
  SELECT order_date, COUNT(*) AS n, SUM(amount) AS revenue
  FROM   staging_orders
  WHERE  order_date >= DATE '2026-11-01' AND order_date < DATE '2026-11-08'
  GROUP  BY order_date
), tgt AS (
  SELECT order_date, COUNT(*) AS n, SUM(amount) AS revenue
  FROM   orders
  WHERE  order_date >= DATE '2026-11-01' AND order_date < DATE '2026-11-08'
  GROUP  BY order_date
)
SELECT COALESCE(s.order_date, t.order_date) AS order_date,
       s.n AS src_rows, t.n AS tgt_rows, s.revenue AS src_revenue, t.revenue AS tgt_revenue
FROM   src s
FULL OUTER JOIN tgt t ON t.order_date = s.order_date      -- a day missing on either side shows up
WHERE  s.n IS DISTINCT FROM t.n
   OR  s.revenue IS DISTINCT FROM t.revenue;
```

- `FULL OUTER JOIN` plus `IS DISTINCT FROM` catches a day that exists on only one side, which
  an inner join and `<>` would both hide.
- `NOT EXISTS` rather than `NOT IN` for the orphan check, for the `NULL` reason in
  [3-2](#3-2-the-not-in-trap-is-distinct-from-coalesce).
- Not every failure should stop the pipeline: a `NULL` key fails the run, an orphan from a
  dimension that has not arrived yet is quarantined or given an unknown member. Which checks
  block, and the tools that run them (dbt tests, Great Expectations), are
  [batch_pipeline.md](./batch_pipeline.md)'s.

---

## 8) Writing Data: MERGE and Upserts ⭐⭐⭐⭐

### 8-1) `MERGE`

**`MERGE` applies a source to a target in one atomic statement: update the rows that match,
insert the ones that do not, optionally delete — the standard way to load a dimension or
apply CDC.**

```sql
-- PostgreSQL 15+ (also Snowflake, BigQuery, Spark SQL on Delta / Iceberg)
MERGE INTO dim_user AS t
USING (
  SELECT user_id, country, updated_at
  FROM  (SELECT s.*, ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY updated_at DESC) AS rn
         FROM   stg_user s) d
  WHERE  rn = 1                                    -- one source row per key
) AS s
ON t.user_id = s.user_id
WHEN MATCHED AND t.country IS DISTINCT FROM s.country THEN
  UPDATE SET country = s.country, updated_at = s.updated_at
WHEN NOT MATCHED THEN
  INSERT (user_id, country, updated_at) VALUES (s.user_id, s.country, s.updated_at);
```

- **Deduplicate the source first.** If two source rows match one target row, PostgreSQL,
  Snowflake (by default) and BigQuery raise an error rather than pick one.
- The `AND … IS DISTINCT FROM …` condition skips no-op updates, which saves writes and keeps
  `updated_at` meaningful.
- `MERGE` is idempotent when the source is: rerunning it with the same source changes nothing.
- **Do not let an old change overwrite a newer one.** If changes can arrive out of order, or a
  rerun replays an older batch, make the version the update condition:
  `WHEN MATCHED AND s.updated_at > t.updated_at THEN UPDATE SET country = s.country,
  updated_at = s.updated_at` (a source sequence number is safer than a timestamp). This
  **replaces** the `IS DISTINCT FROM` filter rather than being added to it. A newer event that
  carries the same country must still move `updated_at` forward. Otherwise, with the target at
  `A` from time 10, an `A` at time 12 is skipped, and a replayed `B` from time 11 then passes
  the guard and overwrites it.

### 8-2) Upsert and partition overwrite

| Technique | Dialect | Use for |
|---|---|---|
| `INSERT … ON CONFLICT (key) DO UPDATE` | PostgreSQL, SQLite | row-level upsert; needs a unique index on `key` |
| `INSERT … ON DUPLICATE KEY UPDATE` | MySQL | the same in MySQL |
| `MERGE` | standard; PostgreSQL 15+, Snowflake, BigQuery, Delta / Iceberg | upsert + delete in one statement |
| `INSERT OVERWRITE … PARTITION (dt = …)` | Hive, Spark SQL | replace a whole partition — the idempotent batch load |
| `DELETE` + `INSERT` in one transaction | any | the portable partition replace |

```sql
-- PostgreSQL: upsert a daily aggregate — rerunning the day overwrites it
INSERT INTO daily_revenue (day, revenue)
SELECT order_date, SUM(amount) FROM orders WHERE order_date = DATE '2026-11-01' GROUP BY order_date
ON CONFLICT (day) DO UPDATE SET revenue = EXCLUDED.revenue;
```

`EXCLUDED` is the row that was proposed for insertion. Why overwrite beats append for batch
loads is [batch_pipeline.md](./batch_pipeline.md)'s topic.

---

## 9) Dates, Times and Time Zones ⭐⭐⭐

### 9-1) Timestamp types

**Store instants in UTC with a time-zone-aware type, and convert to local time only to
derive a reporting date.**

| Meaning | PostgreSQL | BigQuery | Snowflake | Spark SQL |
|---|---|---|---|---|
| an instant (absolute point in time) | `timestamptz` | `TIMESTAMP` | `TIMESTAMP_TZ` / `TIMESTAMP_LTZ` | `TIMESTAMP` (session time zone) |
| a wall-clock time, no zone | `timestamp` | `DATETIME` | `TIMESTAMP_NTZ` | `TIMESTAMP_NTZ` (3.4+) |
| a calendar date | `date` | `DATE` | `DATE` | `DATE` |

```sql
-- PostgreSQL: the Taipei business date of a UTC instant
SELECT (TIMESTAMPTZ '2026-11-01 18:30:00+00' AT TIME ZONE 'Asia/Taipei')::date AS taipei_date;
-- 2026-11-02: half past six in the evening UTC is already the next day in Taipei
```

- A daily report "by date" must say **whose** date. Partitioning UTC data by local date, or the
  reverse, puts events in the wrong day near midnight.
- Use IANA zone names (`Asia/Taipei`, `America/New_York`), not fixed offsets, so daylight saving is handled.

### 9-2) Truncation, ranges and date spines

**Filter time with half-open ranges on the raw column, group with truncation, and generate a
calendar to show days that have no data.**

```sql
-- PostgreSQL: daily active users for November, including days with zero activity
WITH spine AS (
  SELECT generate_series(DATE '2026-11-01', DATE '2026-11-30', INTERVAL '1 day')::date AS day
), dau AS (
  SELECT event_ts::date AS day, COUNT(DISTINCT user_id) AS dau
  FROM   events
  WHERE  event_ts >= TIMESTAMP '2026-11-01'
    AND  event_ts <  TIMESTAMP '2026-12-01'      -- half-open: no 23:59:59.999 edge
  GROUP  BY 1
)
SELECT s.day, COALESCE(d.dau, 0) AS dau
FROM   spine s
LEFT JOIN dau d ON d.day = s.day
ORDER  BY s.day;
```

- `BETWEEN '2026-11-01' AND '2026-11-30'` on a timestamp silently drops the whole last day after midnight.
- `WHERE DATE(event_ts) = …` reads correctly but can defeat an index or partition pruning on
  `event_ts`; the range form keeps the filter on the raw column.
- BigQuery: `GENERATE_DATE_ARRAY` + `UNNEST`; Snowflake: `GENERATOR` + `DATEADD`; Spark:
  `explode(sequence(start, stop, interval 1 day))`.

---

## 10) Query Performance ⭐⭐⭐⭐

### 10-1) Indexes in an OLTP database

**A B-tree index turns a scan into a lookup when the query filters or sorts on the index's
leading columns; it costs space and slows every write.**

- **Composite index `(a, b)`** serves `WHERE a = ?` and `WHERE a = ? AND b = ?`, and
  `ORDER BY a, b`, but generally not `WHERE b = ?` alone — the leftmost-prefix rule
  (PostgreSQL 18 can sometimes skip-scan it).
- Put equality columns first, then the range column: `(user_id, order_date)` serves
  `user_id = ? AND order_date >= ?`.
- **Covering index** — `CREATE INDEX … (user_id) INCLUDE (amount)` answers the query from the
  index alone (an index-only scan).
- **Sargable predicates**: `WHERE created_at >= …` can use an index; `WHERE DATE(created_at) = …`,
  `WHERE amount * 2 > …` or `WHERE col LIKE '%x'` cannot (unless there is an expression index).
- Low-selectivity columns (a boolean, a status with three values) rarely benefit; a **partial
  index** (`WHERE status = 'pending'`) often does.

### 10-2) Performance on a columnar warehouse

**A warehouse has no B-tree indexes to lean on: cost is bytes scanned and data shuffled, so
read fewer columns, prune partitions, and shrink data before joining.**

| Lever | What it does |
|---|---|
| select only needed columns | columnar storage reads only those columns — `SELECT *` reads all of them |
| filter on the partition column | partition pruning skips whole partitions |
| clustering / sort keys | min/max statistics per block let the engine skip blocks |
| filter and aggregate before joining | less data to shuffle |
| broadcast small dimensions | avoids shuffling the fact table |
| materialised views / summary tables | precompute hot aggregations |
| approximate aggregates | sketch instead of exact distinct sets |

- Pruning needs the filter **on the partition column itself**: `WHERE dt = '2026-11-01'` prunes;
  a function around the column may not, depending on the engine.
- BigQuery bills on bytes scanned, so `LIMIT` does not reduce cost on a plain table scan —
  `SELECT * … LIMIT 10` still reads every selected column.

### 10-3) Reading `EXPLAIN`

**`EXPLAIN` shows the plan the optimiser chose; `EXPLAIN ANALYZE` actually runs the query and
adds real row counts and timings — compare estimated with actual rows to find the bad step.**

```sql
-- PostgreSQL
EXPLAIN (ANALYZE, BUFFERS)
SELECT u.country, SUM(o.amount)
FROM   orders o
JOIN   users  u ON u.user_id = o.user_id
WHERE  o.order_date >= DATE '2026-10-01'
GROUP  BY u.country;
```

What to look for:

- **Seq Scan** on a large table where you expected an index — a missing or non-sargable index.
- **Estimated rows far from actual rows** — stale statistics (`ANALYZE` the table) or
  correlated columns; it leads to the wrong join algorithm.
- **Nested Loop** with a large outer side — usually a bad estimate.
- **Sort / Hash spilling to disk** (`external merge`, `Batches: > 1`) — `work_mem` too small
  or too much data reaching that node.
- In a warehouse or Spark plan: the stage with the most bytes shuffled, and whether the join is
  broadcast or shuffle.

### 10-4) Skew and very large joins

**When one key holds a large share of the rows, the worker that owns it does most of the
work; the query is as slow as that one worker.**

- Find it: `SELECT key, COUNT(*) FROM t GROUP BY key ORDER BY 2 DESC LIMIT 20`. The top key is
  often `NULL`, `0`, `'unknown'` or a test account.
- Fixes: filter or handle the hot key separately and `UNION ALL` the result; broadcast the
  small side; let adaptive execution split skewed partitions; salt the key (see [`./spark.md`](./spark.md)).
- `COUNT(DISTINCT)` over a skewed key has the same problem; pre-aggregate with `GROUP BY key,
  value` first, or go approximate.

---

## 11) Dialect Differences ⭐⭐⭐

**The concepts are the same everywhere; the spellings differ. In an interview, name the
dialect you are writing and the interviewer will accept it.**

| Task | PostgreSQL | BigQuery | Snowflake | Spark SQL / Databricks |
|---|---|---|---|---|
| truncate to month | `DATE_TRUNC('month', d)` | `DATE_TRUNC(d, MONTH)` | `DATE_TRUNC('month', d)` | `date_trunc('MONTH', d)` / `trunc(d, 'MM')` |
| difference in days | `d2 - d1` | `DATE_DIFF(d2, d1, DAY)` | `DATEDIFF(day, d1, d2)` | `datediff(d2, d1)` |
| add an interval | `d + INTERVAL '7 days'` | `DATE_ADD(d, INTERVAL 7 DAY)` | `DATEADD(day, 7, d)` | `date_add(d, 7)` |
| filter on a window | subquery / CTE | `QUALIFY` | `QUALIFY` | `QUALIFY` (Databricks) |
| string aggregation | `string_agg(x, ',')` | `STRING_AGG(x, ',')` | `LISTAGG(x, ',')` | `array_join(collect_list(x), ',')` |
| explode an array | `unnest(arr)` | `CROSS JOIN UNNEST(arr)` | `LATERAL FLATTEN(input => arr)` | `explode(arr)` / `LATERAL VIEW explode(arr)` |
| safe division | `x / NULLIF(y, 0)` | `SAFE_DIVIDE(x, y)` | `x / NULLIF(y, 0)` (`DIV0` returns 0) | `try_divide(x, y)` |
| integer `7 / 2` | `3` (integer division) | `3.5` (`DIV(7, 2)` = 3) | `3.5` | `3.5` (`7 div 2` = 3) |
| quoted identifier | `"col"` | `` `col` `` | `"col"` | `` `col` `` |
| case-insensitive match | `ILIKE` | `LOWER(x) LIKE …` | `ILIKE` | `ILIKE` |
| upsert | `ON CONFLICT` / `MERGE` (15+) | `MERGE` | `MERGE` | `MERGE` (Delta / Iceberg) |

The integer-division row is the one that silently produces wrong answers: in PostgreSQL,
`COUNT(*) / total` between two integers is `0` for any rate below 1. Multiply by `1.0` or cast.

---

## 12) Quick Questions ⭐⭐⭐⭐

### 12-1) Short answers to the questions that come up

| Question | Answer |
|---|---|
| `WHERE` vs `HAVING`? | `WHERE` filters rows before grouping; `HAVING` filters groups after, and can use aggregates. |
| `COUNT(*)` vs `COUNT(1)` vs `COUNT(col)`? | The first two are identical (count rows); `COUNT(col)` skips `NULL`s. |
| `UNION` vs `UNION ALL`? | `UNION` removes duplicates at the cost of a sort/hash; `UNION ALL` keeps every row. |
| `RANK` vs `DENSE_RANK` vs `ROW_NUMBER`? | Ties: same rank with gaps / same rank without gaps / arbitrary unique numbers. |
| `DELETE` vs `TRUNCATE` vs `DROP`? | Delete rows (logged, filterable) / remove all rows fast / remove the table itself. |
| Why did my `LEFT JOIN` lose rows? | A `WHERE` condition on the right table discards the `NULL`-extended rows — move it into `ON`. |
| Why did my join return more rows than the left table? | The right side has several rows per key — fan-out; check uniqueness. |
| Why does `NOT IN` return nothing? | The subquery contains a `NULL`; use `NOT EXISTS`. |
| How do you find duplicates? | `GROUP BY key HAVING COUNT(*) > 1`; to remove them, `ROW_NUMBER()` and keep 1. |
| Latest row per key with `MAX(updated_at)` and a join back? | Returns two rows when two share the latest timestamp; use `ROW_NUMBER()` with a tie-breaker. |
| A late change arrived — which version is current? | The one latest by event time (or source sequence), never by arrival time; rebuild the key's intervals with `LEAD`. |
| Is a CTE computed once? | Engine-dependent: PostgreSQL 12+ inlines single-use CTEs, warehouses may recompute a reused one. |
| How do you make a query faster? | Read `EXPLAIN`; filter early on indexed / partition columns; select fewer columns; aggregate before joining; fix skew. |
| A view vs a materialised view? | A view is a stored query run each time; a materialised view stores the result and must be refreshed. |

### 12-2) How to approach a SQL question in the interview

1. **Restate the grain of the answer** — "one row per user per month" — before writing anything.
2. **Ask about the edges**: ties, `NULL`s, users with no activity, time zone of "a day".
3. **Write it as CTEs**, one step each, and name them for what they hold.
4. **Check it on a tiny example** out loud — two users, one tie, one missing day.
5. **Say the cost**: which step shuffles or scans the most, and what index or partition helps.
